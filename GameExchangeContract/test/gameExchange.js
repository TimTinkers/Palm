/**
 * A file to specify tests for the GameExchange.
 *
 * @author Tim Clancy
 * @version 1.0.0
 * @date
 */

// Imports and testing requirements.
import ether from './helpers/ether';
import { advanceBlock } from './helpers/advanceToBlock';
import { increaseTimeTo, duration } from './helpers/increaseTime';
import latestTime from './helpers/latestTime';
import EVMRevert from './helpers/EVMRevert';
const BigNumber = web3.BigNumber;
require('chai')
	.use(require('chai-as-promised'))
	.use(require('chai-bignumber')(BigNumber))
	.should();

// Import the GameExchange token.
const GameExchange = artifacts.require('GameExchange');

// Test the exchange contract.
// The addresses for testing are the game authority allowed to mint and modify 
// records, and the player who can opt in and out of these modifications.
contract('GameExchange', function ([ authority, player, playerTwo ]) {

	// Testing constants.
	const ZERO = new BigNumber(0);
	const ONE = new BigNumber(1);

	// Before conducting any tests, advance to the next block to correctly read 
	// time in the solidity "now" function interpreted by testrpc.
	before(async function () {
		await advanceBlock();
	});

	// Before executing each test case, create a new token 
	// instance with the specified testing parameters.
	beforeEach(async function () {
		this.exchange = await GameExchange.new("GameExchange", "GEX", authority);
	});

	// The exchange should start with no tokens.
	it('Verify exchange is created with no owned tokens.', 
	async function () {

		// Verify the exchange instance exists.
		this.exchange.should.exist;

		// Retrieve counts from the exchange instance.
		const totalCount = await this.exchange.totalSupply();
		const playerCount = await this.exchange.balanceOf(player);

		// Verify that they match the expected values.
		totalCount.should.be.bignumber.equal(ZERO);
		playerCount.should.be.bignumber.equal(ZERO);
	});

	// Tokens can be minted by the trusted authority.
	it('Verify exchange authority can mint tokens.', async function () {
		
		// Verify the exchange starts with no tokens.
		var totalCount = await this.exchange.totalSupply();
		var playerCount = await this.exchange.balanceOf(player);
		totalCount.should.be.bignumber.equal(ZERO);
		playerCount.should.be.bignumber.equal(ZERO);

		// Mint a new token.
		await this.exchange.mint(player, "test", { from: authority })
			.should.be.fulfilled;

		// Verify the correct number of tokens.
		totalCount = await this.exchange.totalSupply();
		playerCount = await this.exchange.balanceOf(player);
		totalCount.should.be.bignumber.equal(ONE);
		playerCount.should.be.bignumber.equal(ONE);

		// Verify the correct token metadata.
		var tokenID = (await this.exchange.tokensOf(player))[0];
		var metadata = await this.exchange.tokenMetadata(tokenID);
		metadata.should.be.a('string').equal("test");
	});

	// Untrusted players cannot mint new tokens.
	it('Verify exchange rejects unauthorized minting.', async function () {
		
		// Verify the exchange starts with no tokens.
		var totalCount = await this.exchange.totalSupply();
		var playerCount = await this.exchange.balanceOf(player);
		totalCount.should.be.bignumber.equal(ZERO);
		playerCount.should.be.bignumber.equal(ZERO);

		// Mint a new token.
		await this.exchange.mint(player, "test", { from: player })
			.should.be.rejectedWith(EVMRevert);
	});

	// Players can opt into metadata control from the authority.
	it('Verify exchange authority can modify metadata when permitted.', async function () {
		
		// Verify the exchange authority starts with no permission to a token.
		await this.exchange.mint(player, "test", { from: authority })
			.should.be.fulfilled;
		var tokenID = (await this.exchange.tokensOf(player))[0];
		var metadata = await this.exchange.tokenMetadata(tokenID);
		var permission = await this.exchange.getOptIn.call(tokenID);
		metadata.should.be.a('string').equal("test");
		permission.should.be.false;

		// Verify players can opt into metadata modification.
		await this.exchange.optIn(tokenID, { from: player })
			.should.be.fulfilled;
		permission = await this.exchange.getOptIn.call(tokenID);
		permission.should.be.true;

		// Verify the exchange authority can update metadata.
		await this.exchange.setTokenMetadata(tokenID, "new data", { from: authority })
			.should.be.fulfilled;
		metadata = await this.exchange.tokenMetadata(tokenID);
		metadata.should.be.a('string').equal("new data");

		// Verify players can opt out of metadata modification.
		await this.exchange.optOut(tokenID, { from: player })
			.should.be.fulfilled;
		permission = await this.exchange.getOptIn.call(tokenID);
		permission.should.be.false;

		// Verify an unpermitted exchange authority cannot update metadata.
		await this.exchange.setTokenMetadata(tokenID, "new data!", { from: authority })
			.should.be.rejectedWith(EVMRevert);
	});

	// Players cannot control their own metadata.
	it('Verify exchange cannot be circumvented by players.', async function () {
		
		// Verify the exchange authority starts with no permission to a token.
		await this.exchange.mint(player, "test", { from: authority })
			.should.be.fulfilled;
		var tokenID = (await this.exchange.tokensOf(player))[0];
		var metadata = await this.exchange.tokenMetadata(tokenID);
		var permission = await this.exchange.getOptIn.call(tokenID);
		metadata.should.be.a('string').equal("test");
		permission.should.be.false;

		// Verify players can opt into metadata modification.
		await this.exchange.optIn(tokenID, { from: player })
			.should.be.fulfilled;
		permission = await this.exchange.getOptIn.call(tokenID);
		permission.should.be.true;

		// Verify the player cannot update metadata.
		await this.exchange.setTokenMetadata(tokenID, "new data", { from: player })
			.should.be.rejectedWith(EVMRevert);
	});

	// The exchange cannot opt players in without their permission.
	it('Verify exchange cannot opt players in.', async function () {
		
		// Verify the exchange authority starts with no permission to a token.
		await this.exchange.mint(player, "test", { from: authority })
			.should.be.fulfilled;
		var tokenID = (await this.exchange.tokensOf(player))[0];
		var metadata = await this.exchange.tokenMetadata(tokenID);
		var permission = await this.exchange.getOptIn.call(tokenID);
		metadata.should.be.a('string').equal("test");
		permission.should.be.false;

		// Verify exchange cannot opt itself into metadata modification.
		await this.exchange.optIn(tokenID, { from: authority })
			.should.be.rejectedWith(EVMRevert);
	});

	// Players cannot transfer tokens with the opt-in active.
	it('Verify exchange cannot transfer opt-in tokens.', async function () {
		
		// Verify the exchange authority starts with no permission to a token.
		await this.exchange.mint(player, "test", { from: authority })
			.should.be.fulfilled;
		var tokenID = (await this.exchange.tokensOf(player))[0];
		var metadata = await this.exchange.tokenMetadata(tokenID);
		var permission = await this.exchange.getOptIn.call(tokenID);
		metadata.should.be.a('string').equal("test");
		permission.should.be.false;

		// Verify players can opt into metadata modification.
		await this.exchange.optIn(tokenID, { from: player })
			.should.be.fulfilled;
		permission = await this.exchange.getOptIn.call(tokenID);
		permission.should.be.true;

		// Verify the player cannot transfer the token.
		await this.exchange.transfer(playerTwo, tokenID, { from: player })
			.should.be.rejectedWith(EVMRevert);

		// Verify that once opted out the token can transfer.
		await this.exchange.optOut(tokenID, { from: player })
			.should.be.fulfilled;
		permission = await this.exchange.getOptIn.call(tokenID);
		permission.should.be.false;
		await this.exchange.transfer(playerTwo, tokenID, { from: player })
			.should.be.fulfilled;

		// Verify transfer success.
		var playerBalance = await this.exchange.balanceOf(player);
		var playerTwoBalance = await this.exchange.balanceOf(playerTwo);
		playerBalance.should.be.bignumber.equal(ZERO);
		playerTwoBalance.should.be.bignumber.equal(ONE);
		tokenID = (await this.exchange.tokensOf(playerTwo))[0];
		metadata = await this.exchange.tokenMetadata(tokenID);
		permission = await this.exchange.getOptIn.call(tokenID);
		metadata.should.be.a('string').equal("test");
		permission.should.be.false;
	});
});