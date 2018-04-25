package us.rockhopper.palm.palm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * A Web3j sample program which demonstrates communication between the Unreal
 * Engine and the Ethereum network. This class is the main entry point for the
 * example server. Networking interface with UE4 is supported by the LowEntry
 * Java socket plugin.
 * 
 * @author Tim Clancy
 * @version 1.0.0
 * @date 4.25.2018
 */
public class Main {

	// A logger for this class.
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	// A path to the configuration file.
	private static final String CONFIGURATION_PATH = "./config/config.txt";

	// Configuration terms for the config.txt flags.
	private static final String NODE_URL_TERM = "nodeURL=";
	private static final String PRIVATE_KEY_TERM = "privateKey=";
	private static final String CONTRACT_ADDRESS_TERM = "contractAddress=";
	private static final String GAS_PRICE_TERM = "gasPrice=";
	private static final String GAS_LIMIT_TERM = "gasLimit=";

	// Configuration options.
	private final static String NODE_URL;
	private final static String PRIVATE_KEY;
	private final static String CONTRACT_ADDRESS;
	private final static BigInteger GAS_PRICE;
	private final static BigInteger GAS_LIMIT;

	// The smart contract to interface with.
	private static GameExchange_sol_GameExchange CONTRACT;

	/**
	 * A static initializer to read and set configuration options.
	 */
	static {

		// Parse the configuration file.
		LOG.info("Parsing configuration files...");
		String nodeURL = "";
		String privateKey = "";
		String contractAddress = "";
		String gasPrice = "";
		String gasLimit = "";
		BufferedReader reader;
		try {

			// Read every line in the configuration file.
			reader = new BufferedReader(
					new FileReader(new File(CONFIGURATION_PATH)));
			String line = "";
			while ((line = reader.readLine()) != null) {

				// Match each line to a configuration variable.
				if (line.startsWith(NODE_URL_TERM)) {
					nodeURL = line.substring(NODE_URL_TERM.length());
				} else if (line.startsWith(PRIVATE_KEY_TERM)) {
					privateKey = line.substring(PRIVATE_KEY_TERM.length());
				} else if (line.startsWith(CONTRACT_ADDRESS_TERM)) {
					contractAddress =
							line.substring(CONTRACT_ADDRESS_TERM.length());
				} else if (line.startsWith(GAS_PRICE_TERM)) {
					gasPrice = line.substring(GAS_PRICE_TERM.length());
				} else if (line.startsWith(GAS_LIMIT_TERM)) {
					gasLimit = line.substring(GAS_LIMIT_TERM.length());
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {

			// Log a potential error when accessing the configuration file.
			LOG.error("Unable to open configuration file.");
			e.printStackTrace();
			throw new IllegalStateException("Must open configuration file.");
		} catch (IOException e) {

			// Log a potential error when reading the configuration file.
			LOG.error("Unable to read from configuration file.");
			e.printStackTrace();
			throw new IllegalStateException("Must read configuration file.");
		}

		// Set the final configuration variables.
		NODE_URL = nodeURL;
		PRIVATE_KEY = privateKey;
		CONTRACT_ADDRESS = contractAddress;
		try {
			GAS_PRICE = BigInteger.valueOf(Long.parseLong(gasPrice));
		} catch (NumberFormatException e) {

			// Log the potentially invalid input value.
			String invalidTerm = "Invalid value for " + GAS_PRICE_TERM;
			LOG.error(invalidTerm);
			e.printStackTrace();
			throw new IllegalArgumentException(invalidTerm);
		}
		try {
			GAS_LIMIT = BigInteger.valueOf(Long.parseLong(gasLimit));
		} catch (NumberFormatException e) {

			// Log the potentially invalid input value.
			String invalidTerm = "Invalid value for " + GAS_LIMIT_TERM;
			LOG.error(invalidTerm);
			e.printStackTrace();
			throw new IllegalArgumentException(invalidTerm);
		}
	}

	/**
	 * The main entry point of the program. This proceeds to establish
	 * connection to the example smart contract for tracking the count, then
	 * open requisite sockets to communicate with UE4.
	 * 
	 * @param args
	 *            input arguments.
	 */
	public static void main(final String[] args) {

		// We start by creating a new web3j instance to connect to remote nodes
		// on the network.
		Web3j web3j = Web3j.build(new HttpService(NODE_URL));
		try {
			LOG.info("Connecting to Ethereum client: "
					+ web3j.web3ClientVersion().send().getWeb3ClientVersion());
		} catch (IOException e) {

			// Log the potential connection exception.
			LOG.error("Unable to connect to Ethereum client.");
			e.printStackTrace();
			throw new IllegalStateException("Must establish connection.");
		}

		// We then need to load our Ethereum wallet file.
		LOG.info("Loading credentials...");
		Credentials credentials = null;
		credentials = Credentials.create(PRIVATE_KEY);

		// Now let's connect to our testing contract.
		LOG.info("Connecting to contract...");
		CONTRACT = GameExchange_sol_GameExchange.load(CONTRACT_ADDRESS, web3j,
				credentials, GAS_PRICE, GAS_LIMIT);

		// Start the server sockets for interfacing with UE4.
		GameServer.start();
	}

	// Send the request for the contract's total supply.
	public static BigInteger getTotalSupply() {
		try {
			BigInteger totalSupply = CONTRACT.totalSupply().send();
			return totalSupply;

			// Log the potential error when receiving the total supply.
		} catch (Exception e) {
			LOG.error("Unable to retrieve total supply.");
			e.printStackTrace();
			throw new IllegalStateException("Must retrieve total supply.");
		}
	}

	// Retrieve the metadata of a token.
	public static String tokenMetadata(BigInteger tokenId) {
		try {
			String metadata = CONTRACT.tokenMetadata(tokenId).send();
			return metadata;

			// Log the potential error when getting the metadata.
		} catch (Exception e) {
			LOG.error("Unable to get token metadata.");
			e.printStackTrace();
			throw new IllegalStateException("Must get the metadata.");
		}
	}

	// Send the request to adjust the metadata of a token.
	public static void setTokenMetadata(BigInteger tokenId,
			String newMetadata) {
		try {
			CONTRACT.setTokenMetadata(tokenId, newMetadata).send();

			// Log the potential error when setting the new metadata.
		} catch (Exception e) {
			LOG.error("Unable to update the metadata.");
			e.printStackTrace();
			throw new IllegalStateException("Must update the metadata.");
		}
	}
}
