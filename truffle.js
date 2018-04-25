require('babel-register');
require('babel-polyfill');

// HDWalletProvider only supports a single account right now.
// See the pending PR on their repo for private-key-usage.
var HDWalletProvider = require("truffle-hdwallet-provider");
var configuration = require("./configuration");

module.exports = {
	solc: {
		optimizer: {
			enabled: true,
			runs: 200
		}
	},
	networks: {
		ganache: {
			host: "localhost",
			port: 8545,
			network_id: "*", // Match any network id
			gas: 4698712
		},
		develop: {
			host: "localhost",
			port: 9545,
			network_id: "*", // Match any network id
			gas: 4698712
		},
		ropsten: {
			provider: new HDWalletProvider(configuration.mnemonic, 
				configuration.nodeURL),
			network_id: 3,
			gas: 4698712
		}
	}
};