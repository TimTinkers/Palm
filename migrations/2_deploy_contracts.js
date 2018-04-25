// Import the exchange to deploy.
const GameExchange = artifacts.require("./GameExchange.sol");

// Import the swapper to deploy.
const SwapAndBurn = artifacts.require("./SwapAndBurn.sol");

// Setup the deployers.
module.exports = function(deployer, network, accounts) {

	// Choose a name, ticker symbol, and authority for the first exchange.
	const nameFrom = "GameExchange";
	const symbolFrom = "GEX";
	const authorityFrom = accounts[0];

	// Choose a name, ticker symbol, and authority for the second exchange.
	const nameTo = "RivalExchange";
	const symbolTo = "REX";
	const authorityTo = accounts[0];

	// Deploy the "from" exchange.
	// deployer.deploy(GameExchange, nameFrom, symbolFrom, authorityFrom, { from: authorityFrom });

	// Deploy the "to" exchange.
	// deployer.deploy(GameExchange, nameTo, symbolTo, authorityTo, { from: authorityTo });

	// Deploy the swapper.
	const addressFrom = "0x5e469871e80474e231af5c252471b6d6817fc990";
	const addressTo = "0x09099905E4F5E8383ee33b843eeEA014BE4F8037";
	const swapperAddress = accounts[0];
	// deployer.deploy(SwapAndBurn, addressFrom, addressTo, { from: swapperAddress });
};