/**
 * A file to specify tests for the SwapAndBurn.
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

// Import the SwapAndBurn contract.
const SwapAndBurn = artifacts.require('SwapAndBurn');

// Test the swap mechanism.
// The addresses for testing are the game authority allowed to mint and modify 
// records for the "from" token, the authority for the "to" token, and a player.
contract('SwapAndBurn', function ([ authorityFrom, authorityTo, player ]) {

	// Testing constants.
	const NAME_FROM = "GameExchange";
	const SYMBOL_FROM = "GEX";
	const NAME_TO = "RivalExchange";
	const SYMBOL_TO = "REX";
	const ZERO = new BigNumber(0);
	const ONE = new BigNumber(1);

	// Before conducting any tests, advance to the next block to correctly read 
	// time in the solidity "now" function interpreted by testrpc.
	before(async function () {
		await advanceBlock();
	});

	// Before executing each test case, create new token 
	// instances with the specified testing parameters.
	beforeEach(async function () {

		// Create the token instances and establish a swapper between them.
		this.exchangeFrom = await GameExchange.new(NAME_FROM, SYMBOL_FROM, authorityFrom);
		this.exchangeTo = await GameExchange.new(NAME_TO, SYMBOL_TO, authorityTo);
		this.swapper = await SwapAndBurn.new(this.exchangeFrom.address, this.exchangeTo.address);

		// Authorize the swapper to mint tokens for the destination "to" token.
		await this.exchangeTo.authorize(this.swapper.address, { from: authorityTo })
			.should.be.fulfilled;
	});

	// The exchanges should start with no tokens.
	it('Verify swapper sees two different exchanges.', 
	async function () {

		// Verify the exchange instances exist.
		this.exchangeFrom.should.exist;
		this.exchangeTo.should.exist;

		// Retrieve counts from the exchange instances.
		const totalCountFrom = await this.exchangeFrom.totalSupply();
		const playerCountFrom = await this.exchangeFrom.balanceOf(player);
		const totalCountTo = await this.exchangeTo.totalSupply();
		const playerCountTo = await this.exchangeTo.balanceOf(player);

		// Verify that they match the expected values.
		totalCountFrom.should.be.bignumber.equal(ZERO);
		playerCountFrom.should.be.bignumber.equal(ZERO);
		totalCountTo.should.be.bignumber.equal(ZERO);
		playerCountTo.should.be.bignumber.equal(ZERO);

		// Retrieve names and symbols from the exchange instances.
		const nameFrom = await this.exchangeFrom.name();
		const symbolFrom = await this.exchangeFrom.symbol();
		const nameTo = await this.exchangeTo.name();
		const symbolTo = await this.exchangeTo.symbol();

		// Verify that they match the expected values.
		nameFrom.should.be.a('string').equal(NAME_FROM);
		symbolFrom.should.be.a('string').equal(SYMBOL_FROM);
		nameTo.should.be.a('string').equal(NAME_TO);
		symbolTo.should.be.a('string').equal(SYMBOL_TO);
	});

	// The swapper should enable destruction of a "from" token for redemption of a "to" token.
	it('Verify swapper destroys and converts.', 
	async function () {

		// Verify the exchanges start with no tokens.
		var totalCountFrom = await this.exchangeFrom.totalSupply();
		var playerCountFrom = await this.exchangeFrom.balanceOf(player);
		var totalCountTo = await this.exchangeTo.totalSupply();
		var playerCountTo = await this.exchangeTo.balanceOf(player);
		totalCountFrom.should.be.bignumber.equal(ZERO);
		playerCountFrom.should.be.bignumber.equal(ZERO);
		totalCountTo.should.be.bignumber.equal(ZERO);
		playerCountTo.should.be.bignumber.equal(ZERO);

		// Mint a new token on the "from" exchange.
		await this.exchangeFrom.mint(player, "test", { from: authorityFrom })
			.should.be.fulfilled;

		// Verify the "from" token exists.
		totalCountFrom = await this.exchangeFrom.totalSupply();
		playerCountFrom = await this.exchangeFrom.balanceOf(player);
		totalCountFrom.should.be.bignumber.equal(ONE);
		playerCountFrom.should.be.bignumber.equal(ONE);

		// Verify the correct "from" token metadata.
		var tokenID = (await this.exchangeFrom.tokensOf(player))[0];
		var metadata = await this.exchangeFrom.tokenMetadata(tokenID);
		metadata.should.be.a('string').equal("test");

		// The owner of the token must approve the swapper to take ownership.
		await this.exchangeFrom.approve(this.swapper.address, tokenID, { from: player })
			.should.be.fulfilled;

		// Now the swap can proceeed.
		await this.swapper.trade(tokenID, { from: player })
			.should.be.fulfilled;

		// Verify the "from" token no longer exists.
		totalCountFrom = await this.exchangeFrom.totalSupply();
		playerCountFrom = await this.exchangeFrom.balanceOf(player);
		totalCountFrom.should.be.bignumber.equal(ZERO);
		playerCountFrom.should.be.bignumber.equal(ZERO);

		// Verify the "to" token exists.
		totalCountTo = await this.exchangeTo.totalSupply();
		playerCountTo = await this.exchangeTo.balanceOf(player);
		totalCountTo.should.be.bignumber.equal(ONE);
		playerCountTo.should.be.bignumber.equal(ONE);

		// Verify the correct "to" token metadata.
		tokenID = (await this.exchangeTo.tokensOf(player))[0];
		metadata = await this.exchangeTo.tokenMetadata(tokenID);
		metadata.should.be.a('string').equal("redeemed");
	});

	// TODO: test the edge-case of attempted trade-sniping.
});