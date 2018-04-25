// Import the exchange to deploy.
const GameExchange = artifacts.require("./GameExchange.sol");

// Setup the GameExchange parameters.
module.exports = function(deployer, network, accounts) {

	// Choose a name and ticker symbol for the asset.
	const name = "GameExchange";
	const symbol = "GEX";

	// Choose the authority address for the game server.
	const authority = accounts[0];

	// Deploy the exchange.
	deployer.deploy(GameExchange, name, symbol, authority);
};