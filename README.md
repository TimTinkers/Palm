# Palm

Palm is the continuation of my work on [Galah](https://github.com/TimTinkers/Galah) to explore new and unique game development capabilities that integrating with the Ethereum blockchain can bring. Through the specific use of the [ERC-721](http://erc721.org/) standard for non-fungible assets, Palm considers just what can be accomplished with globally-available trustless game state.

<p float="left">
  <img width="285" height="285" src="Media/PalmCockatoo.jpg"/>
  <img height="285" src="Media/GalahArchitecture.PNG"/>
</p>

In keeping with the precedent set by Galah, I've named the project after a large Australian parrot.<sup>1</sup>

## Motivation

The multibillion-dollar video game industry is increasingly adopting in-game purchases as an additional revenue source. Such in-game purchases are dubbed "microtransactions" and require players spend real-world money to unlock in-game content.<sup>2</sup> Microtransactions are often criticized by players and have resulted in public relations disasters for the companies which implement them.<sup>3</sup> Using the Ethereum blockchain, this project demonstrates a revision to the microtransaction model where players can purchase and truly own their in-game content.<sup>4,5</sup>

Ethereum is one of several blockchains popular among developers for its ability to execute useful code. With Ethereum, this is done using developer-defined "smart contracts." A developing standard in Ethereum is the ERC-721 "non-fungible token" to unify how contracts represent unique, individually-owned metadata. This standard enables a common "CryptoObject" where developers can store any information their applications use. Ethereum users can take irrevocable ownership of these objects and freely trade them with one another.

This project demonstrates techniques by which the [Unreal Engine](https://www.unrealengine.com/en-US/blog), a popular tool for developing video games, can interface with Ethereum contracts. Now developers can use the CryptoObject contracts to represent, sell, and interact with their in-game content. The controversial microtransaction model is altered: players no longer pay to just unlock content restricted to a single game. Instead, they attain real ownership. Player-owned content can be exchanged with others or used in multiple games.

**Palm demonstrates how Ethereum empowers the free trade of online game content.**

## Exchange Trust Model

Typically, real-time video games simulate their world using a fixed simulation timestep known as the "tick rate." For example, Valve Corporation's popular multiplayer first-person shooter _Counter-Strike: Source_ supports servers with a tick rate of 66Hz.<sup>6</sup> That is, the game servers update state 66 times per second. Game logic, physics simulations, and player input signals are all processed in frequent, discrete time quanta. These state updates are decoupled from the client-side rendering frame rate, allowing for smooth rendering to be maintained across a variety of tick rates.

Real-time games can update their state more frequently than transaction times for Ethereum can currently support. The all-time peak transaction rate for the Ethereum network was 15.6 transactions per second.<sup>7</sup> Even games with less frequent tick rates like Epic Games' _Fortnite_ outpace this peak transaction rate.<sup>8</sup>

It is currently infeasible for a real-time game to track and update its state directly on Ethereum, even if its tick rate was dramatically reduced. The time it takes to interact with Ethereum and wait for a miner to commit a state update transaction to the chain is variable. A fixed tick rate is not currently possible to maintain, which makes processing logic, physics, and input much more difficult. Lastly, operating the game server would require constantly burning gas for small, short-lived updates and would be very costly.

Clearly, traditional servers are more appropriate than Ethereum for handling the frequent game state updates required to simulate a multiplayer game. What role then, if any, can Ethereum play in video games?

|![A hybrid trust model.](Media/opt_in.gif)|
|:-:|
|A hybrid trust model similar to a cryptocurrency exchange can overcome many scalability issues.|

The contracts, web server, and interface shown used in the following demonstration are available in the [GameExchangeContract](https://github.com/TimTinkers/Palm/tree/master/GameExchangeContract) folder of this repository. The interface shown above is a simple page using [web3.js](https://github.com/ethereum/web3.js/) to read state from and interact with a deployed instance of my [GameExchange](https://github.com/TimTinkers/Palm/blob/master/GameExchangeContract/contracts/GameExchange.sol) contract.

The solution that Palm explores is a hybrid trust model where players can opt into and out of object modification from a centralized authority under the control of a game's developers. Instead of a game interacting with a player's on-chain objects in real time, the game can track state changes off-chain on a traditional server. Updates are only committed to the blockchain periodically.

This model is very similar to how large cryptocurrency exchanges operate: when users hold cryptocoins on an exchange, they typically don't own them on-chain. Instead, the off-chain cryptocoin accounting is centralized entirely on the exchange's servers. This model suffers from centralization in that users don't actually fully own their coins until withdrawing from the exchange to another wallet. However, the model benefits from being able to update its off-chain reckoning of state quicker and cheaper than interacting with the blockchain would allow.

While the player has an object opted into modification, they are consciously trusting the game authority to manage state updates to that object appropriately. A malicious or faulty game authority could manipulate the metadata of the object such that it destroys whatever value the object might have held. A malicious cryptocurrency exchange could steal coins in much the same fashion. When the player opts an object out of modification, they lock its state such that not even the game authority can manipulate it.

|![An example game.](Media/gameplay.gif)|
|:-:|
|Palm's example game is a simple shooting gallery where the player's high-scoring gun is tokenized.|

To demonstrate this model in action, Palm includes a simple game which tracks a player's high scores per gun as ERC-721 objects. The game includes a client built in the Unreal Engine and a separate Java server for interacting with the exchange contracts. The Unreal Engine client assets are available in the [TargetShootProject](https://github.com/TimTinkers/Palm/tree/master/TargetShootProject) folder of this repository. The Java server is available in the [TargetShootServer](https://github.com/TimTinkers/Palm/tree/master/TargetShootServer) folder.

The server tracks the player's score and updates the player's record when the match ends if they've broken their high score.

<p align="center">
  <img src="Media/new_highscore.PNG"/>
</p>

...

|![The previous high score is saved, and the new one can overwrite it.](Media/score_updated.gif)|
|:-:|
|The previous high score is retrievable from Ethereum and can be modified by the server when needed.|

...

|![The newer high-score and opting out.](Media/newer_highscore.gif)|
|:-:|
|Showing the even-higher high score from the second round, as well as the user opting-out.|

To prevent assets from being modified without the buyer's knowledge or consent, players can only transfer assets which are opted-out of modification. This model allows the buyer to know for certain that whatever asset they purchase will be theirs in exactly the same state it was sold in. The edge-case where a buyer sees their recently-purchased record change because the seller is still playing with it has been handled.

## Using Assets in Multiple Games

## Trustless Economy

["GameExchange"](https://ropsten.etherscan.io/address/0x5e469871e80474e231af5c252471b6d6817fc990)
["RivalExchange"](https://ropsten.etherscan.io/address/0x09099905e4f5e8383ee33b843eeea014be4f8037)
["SwapAndBurn"](https://ropsten.etherscan.io/address/0x6e6af08a1fa2fd0837dbdd01448c8ec36f63ec29)

## References
The following resources are important references for the information presented in this project:
1. The Palm Cockatoo image is the work of [Reg Mckenna](https://www.flickr.com/photos/whiskymac/), released [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/).
2. Davidovici-Nora, M.: Paid and Free Digital Business Models Innovations in the Video Game Industry. Institut Mines-Telecom, 83-102 (2014).
3. [Most Downvoted Comment](https://www.pcgamer.com/the-most-downvoted-comment-in-reddit-history-is-now-a-star-wars-battlefront-2-mod/), [Electronic Arts](https://www.ea.com/) received massive microtransaction backlash in this [reddit thread](https://www.reddit.com/r/StarWarsBattlefront/comments/7cff0b/seriously_i_paid_80_to_have_vader_locked/).
4. Olsson, B., Sidenblom, L.: Business Models for Video Games. Department of Informatics, Lund University, 5-50 (2010).
5. Švelch, J.: Playing with and against Microtransactions. The Evolution and Social Impact of Video Game Economics. p. 102-120 Lexington Books, London (2017).
6. [Valve Networking Guide](https://developer.valvesoftware.com/wiki/Source_Multiplayer_Networking), an excellent primer on multiplayer game networking with specifics for Valve titles.
7. [Ethereum Transaction Rate](https://etherscan.io/chart/tx), as of 4/25/2018 the rate peaked at 1,349,890 transactions on 1/4/2018.
8. [Battle Royale Tick Rates](https://www.youtube.com/watch?v=u0dWDFDUF8s), an analysis of the tick rates in several multiplayer games of the battle royale genre.

## Supporting Projects
I'd like to thank the following guides, tools, and projects which greatly supported the development of Palm:
- [ERC-721](http://erc721.org/), a good primer on the developing Ethereum standard.
- [Unreal Engine 4](https://www.unrealengine.com/en-US/blog), the Unreal Engine is a free high-quality game and physics engine.
- [Ethereum JavaScript API](https://github.com/ethereum/web3.js/), web3.js provides the wrappers needed to integrate with smart contracts on the web interface.
- [The Online ABI Encoding Tool by HashEx](https://abi.hashex.org/), to convert constructor parameters to ABI encoding for verification.
- [Etherscan](etherscan.io), for providing an easy interface to validate deployment and contract state.
- [JavaScript Promises in Web3](http://shawntabrizi.com/crypto/making-web3-js-work-asynchronously-javascript-promises-await/), this article provides an overview on converting Web3 calls to Promises seamlessly.
- [Truffle](https://github.com/trufflesuite/truffle), a development environment, testing framework and asset pipeline for Ethereum.
- [Infura](https://infura.io/), a gateway for cloud-hosted Ethereum nodes.
- [Web3j](https://web3j.io/), a lightweight, reactive, type-safe Java and Android library for integrating with nodes on Ethereum blockchains.
- [LowEntry Socket Connection](https://www.unrealengine.com/marketplace/low-entry-socket-connection), a useful networking plugin for the Unreal Engine with native Java integration.
- [json-simple](https://github.com/fangyidong/json-simple), a simple and fast JSON parser.
- [solc-js](https://github.com/ethereum/solc-js), JavaScript Solidity compiler bindings used here to create the Java contract wrapper.
