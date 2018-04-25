package us.rockhopper.palm.palm;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lowentry.ue4.classes.sockets.LatentResponse;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.classes.sockets.SocketServer;
import lowentry.ue4.classes.sockets.SocketServerListener;
import lowentry.ue4.library.LowEntry;

/**
 * The main class for the UE4 demo server. This server responds via JSON packets
 * to UE4 actions which retrieve and modify the value of our smart contract
 * counter.
 * 
 * @author Tim Clancy
 * @version 1.0.0
 * @date 4.25.2018
 */
public class GameServer {

	// A logger for this class.
	private static final Logger LOG = LoggerFactory.getLogger(GameServer.class);

	// TCP and UDP ports.
	private static final int PORT_UDP = 33333;
	private static final int PORT_TCP = 33333;

	// Track the user's score and highscore with this asset.
	static BigInteger TOKEN_ID = null;
	static int HIGHSCORE = -1;
	static int SCORE = 0;

	/**
	 * Starts the UE4-interaction server, to be called once connection to the
	 * smart contract has been properly established.
	 */
	public static void start() {

		// Create a JSON parser for use by the networking methods.
		JSONParser parser = new JSONParser();

		// Create a listener for the server's socket.
		SocketServerListener listener = new SocketServerListener() {

			@Override
			/**
			 * This method executes when a client connects to this SocketServer.
			 *
			 * @param server
			 *            the server listening to this socket.
			 * @param client
			 *            the client connection which connected.
			 */
			public void clientConnected(final SocketServer server,
					final SocketClient client) {
				LOG.info("[" + Thread.currentThread().getName()
						+ "] ClientConnected: " + client);
			}

			@Override
			/**
			 * This method executes when a client disconnects from this
			 * SocketServer.
			 *
			 * @param server
			 *            the server listening to this socket.
			 * @param client
			 *            the client connection which disconnected.
			 */
			public void clientDisconnected(final SocketServer server,
					final SocketClient client) {
				LOG.info("[" + Thread.currentThread().getName()
						+ "] ClientDisconnected: " + client);
			}

			@Override
			/**
			 * This method executes when a client is validating the connection.
			 *
			 * @param server
			 *            the server listening to this socket.
			 * @param client
			 *            the client connection which is validating.
			 */
			public void receivedConnectionValidation(SocketServer server,
					SocketClient client) {
			}

			@Override
			/**
			 * This method executes when the server starts receiving an
			 * unreliable (UDP) message packet.
			 *
			 * @param server
			 *            the server listening to this socket and receiving.
			 * @param client
			 *            the client connection which is sending.
			 * @param bytes
			 *            the size in bytes of the message.
			 * @return true to start receiving the packer, false to disconnect
			 *         the client.
			 */
			public boolean startReceivingUnreliableMessage(SocketServer server,
					SocketClient client, int bytes) {
				return (bytes <= (1 * 1024));
			}

			@Override
			/**
			 * This method executes when the server has received an unreliable
			 * (UDP) message packet.
			 *
			 * @param server
			 *            the server listening to this socket and receiving.
			 * @param client
			 *            the client connection which is sending.
			 * @param bytes
			 *            the buffer of bytes in the message.
			 */
			public void receivedUnreliableMessage(SocketServer server,
					SocketClient client, ByteBuffer bytes) {

				// Parse this message for JSON String and sender.
				String bufferString = LowEntry.bytesToStringUtf8(
						LowEntry.getBytesFromByteBuffer(bytes));

				// Parse the message into JSON format and take action.
				try {
					JSONObject jsonMessage =
							(JSONObject) parser.parse(bufferString);

					// Switch on hitting a red or a green target.
					String actionType = (String) jsonMessage.get("action");
					switch (actionType.toLowerCase()) {

					// The player hit a red target, increase their score.
					case "red": {
						SCORE += 1;
						break;
					}

					// The player hit a green target, decrease their score.
					case "green": {
						SCORE -= 1;
						break;
					}

					// The player has finished. Log a potential new high score.
					case "end": {
						if (SCORE > HIGHSCORE) {
							Main.setTokenMetadata(TOKEN_ID, "" + SCORE);
						}
						SCORE = 0;
						break;
					}
					}
				} catch (ParseException e) {

					// Log a potential error with parsing the packet.
					LOG.error("Bad packet from UE4.");
					e.printStackTrace();
				}
			}

			@Override
			/**
			 * This method executes when the server starts receiving a reliable
			 * message packet (TCP).
			 *
			 * @param server
			 *            the server listening to this socket and receiving.
			 * @param client
			 *            the client connection which is sending.
			 * @param bytes
			 *            the size in bytes of the message.
			 * @return true to start receiving the packer, false to disconnect
			 *         the client.
			 */
			public boolean startReceivingMessage(final SocketServer server,
					final SocketClient client, final int bytes) {
				return (bytes <= (10 * 1024));
			}

			@Override
			/**
			 * This method executes when the server has received a reliable
			 * message packet (TCP).
			 *
			 * @param server
			 *            the server listening to this socket and receiving.
			 * @param client
			 *            the client connection which is sending.
			 * @param bytes
			 *            the array of bytes in the message.
			 */
			public void receivedMessage(final SocketServer server,
					final SocketClient client, final byte[] bytes) {
			}

			@Override
			/**
			 * This method executes when the server starts receiving a packet
			 * for a function call.
			 *
			 * @param server
			 *            the server listening to this socket and receiving.
			 * @param client
			 *            the client connection which is sending.
			 * @param bytes
			 *            the size in bytes of the message.
			 * @return true to start receiving the packer, false to disconnect
			 *         the client.
			 */
			public boolean startReceivingFunctionCall(final SocketServer server,
					final SocketClient client, final int bytes) {
				return (bytes <= (10 * 1024));
			}

			@SuppressWarnings("unchecked")
			@Override
			/**
			 * This method executes when the server has received a packet for a
			 * function call.
			 *
			 * @param server
			 *            the server listening to this socket and receiving.
			 * @param client
			 *            the client connection which is sending.
			 * @param bytes
			 *            the array of bytes in the message.
			 */
			public byte[] receivedFunctionCall(final SocketServer server,
					final SocketClient client, final byte[] bytes) {

				// Parse and display the message as a String.
				String bufferString = LowEntry.bytesToStringUtf8(bytes);
				String playerIP = client.pyro().getRemoteAddress().toString();
				LOG.info("[" + Thread.currentThread().getName()
						+ "] Received Function Call from " + playerIP + ": "
						+ bufferString);

				// Parse the message into JSON format and return a response.
				byte[] response = LowEntry.stringToBytesUtf8("");
				try {
					JSONObject jsonMessage =
							(JSONObject) parser.parse(bufferString);

					// Perform the action requested by the client.
					String actionType = (String) jsonMessage.get("action");
					String tokenIdString = (String) jsonMessage.get("tokenId");
					if (TOKEN_ID == null) {
						TOKEN_ID = BigInteger
								.valueOf(Long.parseLong(tokenIdString));
					}
					switch (actionType.toLowerCase()) {
					case "read": {

						// The user is requesting their high-score for a token
						// for the first time.
						if (HIGHSCORE == -1) {
							String metadataString =
									Main.tokenMetadata(TOKEN_ID);
							HIGHSCORE = Integer.parseInt(metadataString);
						}

						// Use the off-chain score if it is higher.
						int sendScore = HIGHSCORE;
						if (SCORE > HIGHSCORE) {
							sendScore = SCORE;
						}

						// Construct the object for UE4 to handle.
						JSONObject countJSON = new JSONObject();
						countJSON.put("action", "read");
						countJSON.put("score", sendScore);
						response = LowEntry
								.stringToBytesUtf8(countJSON.toJSONString());
						break;
					}
					}
				} catch (ParseException e) {

					// Log a potential error with parsing the packet.
					LOG.error("Bad packet from UE4.");
					e.printStackTrace();
				}
				return response;
			}

			@Override
			/**
			 * This method executes when the server starts receiving a packet
			 * for a latent function call.
			 *
			 * @param server
			 *            the server listening to this socket and receiving.
			 * @param client
			 *            the client connection which is sending.
			 * @param bytes
			 *            the size in bytes of the message.
			 * @return true to start receiving the packer, false to disconnect
			 *         the client.
			 */
			public boolean startReceivingLatentFunctionCall(
					final SocketServer server, final SocketClient client,
					final int bytes) {
				return (bytes <= (10 * 1024));
			}

			@Override
			/**
			 * This method executes when the server has received a packet for a
			 * function call.
			 *
			 * @param server
			 *            the server listening to this socket and receiving.
			 * @param client
			 *            the client connection which is sending.
			 * @param bytes
			 *            the array of bytes in the message.
			 * @param response
			 *            the latent response data.
			 */
			public void receivedLatentFunctionCall(final SocketServer server,
					final SocketClient client, final byte[] bytes,
					final LatentResponse response) {
				response.done(null);
			}
		};

		// Starts the SocketServer based on our listener at the specified port.
		// Change the boolean to true if you want to accept external
		// connections, remove the second port to not listen to UDP.
		SocketServer server;
		try {
			server = new SocketServer(false, PORT_UDP, PORT_TCP, listener);
		} catch (Throwable e) {

			// Log potential error with starting the server.
			LOG.error("Failed to start server.");
			e.printStackTrace();
			throw new IllegalStateException("Failed to start server.");
		}

		// Constantly listen to UE4 interactions until termination.
		LOG.info("Game server listening.");
		while (true) {
			server.listen();
		}
	}
}
