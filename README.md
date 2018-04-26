# Palm

Palm is the continuation of my work on [Galah](https://github.com/TimTinkers/Galah) to explore new and unique game development capabilities that integrating with the Ethereum blockchain can bring. Through the specific use of the [ERC-721](http://erc721.org/) standard for non-fungible assets, Palm considers just what can be accomplished with globally-available trustless game state.

<p float="left">
  <img width="285" height="285" src="Media/PalmCockatoo.jpg"/>
  <img height="285" src="Media/GalahArchitecture.PNG"/>
</p>

In keeping with the precedent set by Galah, I've named the project after a large Australian parrot.<sup>1</sup>

## Exchange Trust Model

Typically, real-time video games simulate their world using a fixed simulation timestep known as the "tick rate." For example, Valve Corporation's popular multiplayer first-person shooter _Counter-Strike: Source_ supports servers with a tick rate of 66Hz.<sup>2</sup> That is, the game servers update state 66 times per second. Game logic, physics simulations, and player input signals are all processed in frequent, discrete time quanta. These state updates are decoupled from the client-side rendering frame rate, allowing for smooth rendering to be maintained across a variety of tick rates.

Real-time games can update their state more frequently than transaction times for Ethereum can currently support. The all-time peak transaction rate for the Ethereum network was 15.6 transactions per second.<sup>3</sup> Even games with less frequent tick rates like Epic Games' _Fortnite_ outpace this peak transaction rate.<sup>4</sup>

It is currently infeasible for a real-time game to track and update its state directly on Ethereum, even if its tick rate was dramatically reduced. The time it takes to interact with Ethereum and wait for a miner to commit a state update transaction to the chain is variable. A fixed tick rate is not currently possible to maintain, which makes processing logic, physics, and input much more difficult. Lastly, operating the game server would require constantly burning gas for small, short-lived updates and would be very costly.

Clearly, traditional servers are more appropriate than Ethereum for handling the frequent game state updates required to simulate a multiplayer game. What role then, if any, can Ethereum play in video games?

|![A hybrid trust model.](Media/opt_in.gif)|
|:-:|
|A hybrid trust model similar to a cryptocurrency exchange can overcome many scalability issues.|

The solution that Palm explores is a hybrid trust model where players can opt into and out of asset modification from a centralized authority under the control of a game's developers. What this means is...

|![An example game.](Media/gameplay.gif)|
|:-:|
|Palm's example game is a simple shooting gallery where the player's high-scoring gun is tokenized.|

...

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
2. [Valve Networking Guide](https://developer.valvesoftware.com/wiki/Source_Multiplayer_Networking), an excellent primer on multiplayer game networking with specifics for Valve titles.
3. [Ethereum Transaction Rate](https://etherscan.io/chart/tx), as of 4/25/2018 the rate peaked at 1,349,890 transactions on 1/4/2018.
4. [Battle Royale Tick Rates](https://www.youtube.com/watch?v=u0dWDFDUF8s), an analysis of the tick rates in several multiplayer games of the battle royale genre.

## Supporting Projects
I'd like to thank the following guides, tools, and projects which greatly supported the development of Palm:
- [ERC-721](http://erc721.org/), a good primer on the developing Ethereum standard.
- [The Online ABI Encoding Tool by HashEx](https://abi.hashex.org/), to convert constructor parameters to ABI encoding for verification.
- [Etherscan](etherscan.io), for providing an easy interface to validate deployment and contract state.
- [JavaScript Promises in Web3](http://shawntabrizi.com/crypto/making-web3-js-work-asynchronously-javascript-promises-await/), this article provides an overview on converting Web3 calls to Promises seamlessly.
- [Truffle](https://github.com/trufflesuite/truffle), a development environment, testing framework and asset pipeline for Ethereum.
- [Infura](https://infura.io/), a gateway for cloud-hosted Ethereum nodes.
- [Web3j](https://web3j.io/), a lightweight, reactive, type-safe Java and Android library for integrating with nodes on Ethereum blockchains.
- [LowEntry Socket Connection](https://www.unrealengine.com/marketplace/low-entry-socket-connection), a useful networking plugin for the Unreal Engine with native Java integration.
- [json-simple](https://github.com/fangyidong/json-simple), a simple and fast JSON parser.
- [solc-js](https://github.com/ethereum/solc-js), JavaScript Solidity compiler bindings used here to create the Java contract wrapper.
- [Unreal Engine 4](https://www.unrealengine.com/en-US/blog), the Unreal Engine is a free high-quality game and physics engine.
