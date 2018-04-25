// The demo cannot proceed if Web3 is not present.
if (typeof window.web3 === "undefined") {
	console.error("Web3 is required.");
	alert("Web3 is required. Try downloading MetaMask?");

// Set the default Web3 provider and account.
} else {
	var web3 = new Web3(window.web3.currentProvider);
	web3.eth.defaultAccount = window.web3.eth.defaultAccount;
}

// A helper which can seamlessly convert contract calls to Promises. Thanks to:
// https://ethereum.stackexchange.com/questions/11444/web3-js-with-promisified-api/24238#24238
// http://shawntabrizi.com/crypto/making-web3-js-work-asynchronously-javascript-promises-await/
const promisify = (inner) =>
new Promise((resolve, reject) =>
	inner((err, res) => {
		if (err) {
			reject(err);
		} else {
			resolve(res);
		}
	})
);

// A function which asynchronously sets up the page.
var setup = async function (config) {

	// Connect to and read our deployed GameExchange instances.
	var GameExchangeContract = web3.eth.contract(config.exchangeContract);
	var FromExchange = GameExchangeContract.at(config.fromAddress);
	var fromName = await promisify(cb => FromExchange.name(cb));
	console.log("Successfully loaded connection to exchange: " + fromName);
	var ToExchange = GameExchangeContract.at(config.toAddress);
	var toName = await promisify(cb => ToExchange.name(cb));
	console.log("Successfully loaded connection to exchange: " + toName);

	// Connect to the SwapAndBurn trading instance.
	var SwapAndBurnContract = web3.eth.contract(config.swapContract);
	var SwapAndBurn = SwapAndBurnContract.at(config.swapAddress);

	/*
	// A one-time administrative requirement to authorize the swapping contract as a "to" minter.
	var gasLimit = await promisify(cb => ToExchange.authorize.estimateGas(config.swapAddress, { from: web3.eth.defaultAccount }, cb));
	var transactionData = {
		from: web3.eth.defaultAccount,
		gas: gasLimit,
		gasPrice: 21000000000
	};
	await promisify(cb => ToExchange.authorize.sendTransaction(config.swapAddress, transactionData, cb));
	*/

	// Assign functionality to the example mint button.
	// This simplified example only works because I've made my MetaMask wallet an authority.
	$("#mintButton").click(async function() {
		var metadataString = $("#metadataInput").val();
		var gasLimit = await promisify(cb => FromExchange.mint.estimateGas(web3.eth.defaultAccount, metadataString, { from: web3.eth.defaultAccount }, cb));
		const transactionData = {
			from: web3.eth.defaultAccount,
			gas: gasLimit,
			gasPrice: 21000000000
		};
		await promisify(cb => FromExchange.mint.sendTransaction(web3.eth.defaultAccount, metadataString, transactionData, cb));
	});

	// Allow the user to opt a token into server-controlled mutability.
	$("#optInButton").click(async function() {
		var tokenID = $("#optIdInput").val();
		var gasLimit = await promisify(cb => FromExchange.optIn.estimateGas(tokenID, { from: web3.eth.defaultAccount }, cb));
		var transactionData = {
			from: web3.eth.defaultAccount,
			gas: gasLimit,
			gasPrice: 21000000000
		};
		await promisify(cb => FromExchange.optIn.sendTransaction(tokenID, transactionData, cb));
	});

	// Allow the user to opt a token out of server-controlled mutability.
	$("#optOutButton").click(async function() {
		var tokenID = $("#optIdInput").val();
		var gasLimit = await promisify(cb => FromExchange.optOut.estimateGas(tokenID, { from: web3.eth.defaultAccount }, cb));
		var transactionData = {
			from: web3.eth.defaultAccount,
			gas: gasLimit,
			gasPrice: 21000000000
		};
		await promisify(cb => FromExchange.optOut.sendTransaction(tokenID, transactionData, cb));
	});

	// Assign functionality to the example approval button.
	// Before the user can trigger a trade, they must approve interaction with the trade contract.
	$("#approveButton").click(async function() {
		var tokenID = $("#tokenInput").val();
		var gasLimit = await promisify(cb => FromExchange.approve.estimateGas(config.swapAddress, tokenID, { from: web3.eth.defaultAccount }, cb));
		var transactionData = {
			from: web3.eth.defaultAccount,
			gas: gasLimit,
			gasPrice: 21000000000
		};
		await promisify(cb => FromExchange.approve.sendTransaction(config.swapAddress, tokenID, transactionData, cb));
	});

	// Assign functionality to the example trade button.
	$("#tradeButton").click(async function() {
		var tokenID = $("#tokenInput").val();
		var gasLimit = await promisify(cb => SwapAndBurn.trade.estimateGas(tokenID, { from: web3.eth.defaultAccount }, cb));
		var transactionData = {
			from: web3.eth.defaultAccount,
			gas: gasLimit,
			gasPrice: 21000000000
		};
		await promisify(cb => SwapAndBurn.trade.sendTransaction(tokenID, transactionData, cb));
	});

	// Poll the exchanges every few seconds to update the dashboard.
	var updateStatus = async function () {

		// Update the display of total supply for each exchange.
		var fromTotalSupply = await promisify(cb => FromExchange.totalSupply(cb));
		var toTotalSupply = await promisify(cb => ToExchange.totalSupply(cb));
		$("#totalSizeFrom").text("The exchange contains " + fromTotalSupply + " assets in total");
		$("#totalSizeTo").text("The exchange contains " + toTotalSupply + " assets in total");

		// Update the list of this user's assets on the "from" exchange.
		var fromTokens = await promisify(cb => FromExchange.tokensOf(web3.eth.defaultAccount, cb));
		var updatedListFrom = $("<ul id=\"ownedListFrom\" style=\"list-style-type:circle\"></ul>");
		for (var i = 0; i < fromTokens.length; i++) {
			var tokenID = new web3.BigNumber(fromTokens[i]);
			var metadataString = await promisify(cb => FromExchange.tokenMetadata(tokenID, cb));
			var optStatus = await promisify(cb => FromExchange.getOptIn(tokenID, cb));

			// Color the entry red if it's been approved for transfer.
			var approvedAddress = await promisify(cb => FromExchange.approvedFor(tokenID, cb));
			if (approvedAddress.toUpperCase() === config.swapAddress.toUpperCase()) {
				updatedListFrom.append("<li style=\"color:red;\">" + tokenID + ", opt-in=" + optStatus + ": " + metadataString + "</li>");
			} else {
				updatedListFrom.append("<li>" + tokenID + ", opt-in=" + optStatus + ": " + metadataString + "</li>");
			}
		}
		$("#ownedListFrom").html(updatedListFrom.html());

		// Update the list of this user's assets on the "to" exchange.
		var toTokens = await promisify(cb => ToExchange.tokensOf(web3.eth.defaultAccount, cb));
		var updatedListTo = $("<ul id=\"ownedListFrom\" style=\"list-style-type:circle\"></ul>");
		for (var i = 0; i < toTokens.length; i++) {
			var tokenID = new web3.BigNumber(toTokens[i]);
			var metadataString = await promisify(cb => ToExchange.tokenMetadata(tokenID, cb));
			updatedListTo.append("<li>" + tokenID + ": " + metadataString + "</li>");
		}
		$("#ownedListTo").html(updatedListTo.html());
	};
	updateStatus();
	setInterval(updateStatus, 5000);
}

// Parse the configuration file and pass to setup.
$.getJSON("js/config.json", function (config) {
	setup(config);
});