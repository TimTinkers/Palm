package us.rockhopper.palm.palm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class GameExchange_sol_GameExchange extends Contract {
    private static final String BINARY = "6060604052600060075534156200001557600080fd5b604051620014b9380380620014b983398101604052808051820191906020018051820191906020018051915060059050838051620000589291602001906200009a565b5060068280516200006e9291602001906200009a565b50600160a060020a03166000908152600960205260409020805460ff19166001179055506200013f9050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620000dd57805160ff19168380011785556200010d565b828001600101855582156200010d579182015b828111156200010d578251825591602001919060010190620000f0565b506200011b9291506200011f565b5090565b6200013c91905b808211156200011b576000815560010162000126565b90565b61136a806200014f6000396000f30060606040526004361061011c5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde038114610121578063095ea7b3146101ab57806318160ddd146101cf5780632a6dd48f146101f45780632f745c591461022657806336130f001461024857806342966c681461025e57806344dc6e1a14610274578063510ef1de1461028a5780635a3f2672146102e05780636352211e146103525780636914db601461036857806370a082311461037e57806395d89b411461039d578063a2718305146103b0578063a9059cbb146103da578063b09f1266146103fc578063b2e6ceeb1461040f578063b6a5d7de14610425578063d0def52114610444578063d28d8852146104a3575b600080fd5b341561012c57600080fd5b6101346104b6565b60405160208082528190810183818151815260200191508051906020019080838360005b83811015610170578082015183820152602001610158565b50505050905090810190601f16801561019d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101b657600080fd5b6101cd600160a060020a036004351660243561055f565b005b34156101da57600080fd5b6101e2610651565b60405190815260200160405180910390f35b34156101ff57600080fd5b61020a600435610657565b604051600160a060020a03909116815260200160405180910390f35b341561023157600080fd5b6101e2600160a060020a0360043516602435610672565b341561025357600080fd5b6101cd6004356106ad565b341561026957600080fd5b6101cd6004356106f0565b341561027f57600080fd5b6101cd600435610724565b341561029557600080fd5b6101cd600480359060446024803590810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061076495505050505050565b34156102eb57600080fd5b6102ff600160a060020a0360043516610883565b60405160208082528190810183818151815260200191508051906020019060200280838360005b8381101561033e578082015183820152602001610326565b505050509050019250505060405180910390f35b341561035d57600080fd5b61020a600435610906565b341561037357600080fd5b610134600435610930565b341561038957600080fd5b6101e2600160a060020a03600435166109e1565b34156103a857600080fd5b6101346109fc565b34156103bb57600080fd5b6103c6600435610a6f565b604051901515815260200160405180910390f35b34156103e557600080fd5b6101cd600160a060020a0360043516602435610a84565b341561040757600080fd5b610134610acd565b341561041a57600080fd5b6101cd600435610b6b565b341561043057600080fd5b6101cd600160a060020a0360043516610b96565b341561044f57600080fd5b6101cd60048035600160a060020a03169060446024803590810190830135806020601f82018190048102016040519081016040528181529291906020840183838082843750949650610be495505050505050565b34156104ae57600080fd5b610134610cfe565b6104be611274565b60058054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105545780601f1061052957610100808354040283529160200191610554565b820191906000526020600020905b81548152906001019060200180831161053757829003601f168201915b505050505090505b90565b60008133600160a060020a031661057582610906565b600160a060020a03161461058857600080fd5b61059183610906565b9150600160a060020a0384811690831614156105ac57600080fd5b6105b583610657565b600160a060020a03161515806105d35750600160a060020a03841615155b1561064b5760008381526002602052604090819020805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0387811691821790925591908416907f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259086905190815260200160405180910390a35b50505050565b60005490565b600090815260026020526040902054600160a060020a031690565b600061067d836109e1565b821061068857600080fd5b61069183610883565b828151811061069c57fe5b906020019060200201519392505050565b8033600160a060020a03166106c182610906565b600160a060020a0316146106d457600080fd5b506000908152600a60205260409020805460ff19166001179055565b8033600160a060020a031661070482610906565b600160a060020a03161461071757600080fd5b61072082610d69565b5050565b8033600160a060020a031661073882610906565b600160a060020a03161461074b57600080fd5b506000908152600a60205260409020805460ff19169055565b600160a060020a03331660009081526009602052604090205460ff16151560011461078e57600080fd5b61079782610a6f565b15156001146107a557600080fd5b60008281526008602052604090208180516107c4929160200190611286565b507f09e9976d8c3e4d232147efa3e62e4af75ea58a39b0cdbc3f351d96fec3ed07d56107ef83610906565b8383604051600160a060020a03841681526020810183905260606040820181815290820183818151815260200191508051906020019080838360005b8381101561084357808201518382015260200161082b565b50505050905090810190601f1680156108705780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a15050565b61088b611274565b6003600083600160a060020a0316600160a060020a031681526020019081526020016000208054806020026020016040519081016040528092919081815260200182805480156108fa57602002820191906000526020600020905b8154815260200190600101908083116108e6575b50505050509050919050565b600081815260016020526040812054600160a060020a031680151561092a57600080fd5b92915050565b610938611274565b600860008381526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108fa5780601f106109b4576101008083540402835291602001916108fa565b820191906000526020600020905b8154815290600101906020018083116109c25750939695505050505050565b600160a060020a031660009081526003602052604090205490565b610a04611274565b60068054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105545780601f1061052957610100808354040283529160200191610554565b6000908152600a602052604090205460ff1690565b8033600160a060020a0316610a9882610906565b600160a060020a031614610aab57600080fd5b610ab482610a6f565b15610abe57600080fd5b610ac88383610dfe565b505050565b60068054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b635780601f10610b3857610100808354040283529160200191610b63565b820191906000526020600020905b815481529060010190602001808311610b4657829003601f168201915b505050505081565b610b753382610e30565b1515610b8057600080fd5b610b93610b8c82610906565b3383610e56565b50565b600160a060020a03331660009081526009602052604090205460ff161515600114610bc057600080fd5b600160a060020a03166000908152600960205260409020805460ff19166001179055565b600160a060020a03331660009081526009602052604090205460ff161515600114610c0e57600080fd5b610c1a82600754610f1c565b6007546000908152600860205260409020818051610c3c929160200190611286565b507f09e9976d8c3e4d232147efa3e62e4af75ea58a39b0cdbc3f351d96fec3ed07d58260075483604051600160a060020a03841681526020810183905260606040820181815290820183818151815260200191508051906020019080838360005b83811015610cb5578082015183820152602001610c9d565b50505050905090810190601f168015610ce25780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a15050600780546001019055565b60058054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b635780601f10610b3857610100808354040283529160200191610b63565b8033600160a060020a0316610d7d82610906565b600160a060020a031614610d9057600080fd5b610d9982610657565b600160a060020a031615610db157610db13383610f7e565b610dbb3383611010565b600033600160a060020a03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8460405190815260200160405180910390a35050565b8033600160a060020a0316610e1282610906565b600160a060020a031614610e2557600080fd5b610ac8338484610e56565b600082600160a060020a0316610e4583610657565b600160a060020a0316149392505050565b600160a060020a0382161515610e6b57600080fd5b610e7481610906565b600160a060020a0383811691161415610e8c57600080fd5b82600160a060020a0316610e9f82610906565b600160a060020a031614610eb257600080fd5b610ebc8382610f7e565b610ec68382611010565b610ed08282611187565b81600160a060020a031683600160a060020a03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8360405190815260200160405180910390a3505050565b600160a060020a0382161515610f3157600080fd5b610f3b8282611187565b81600160a060020a031660007fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8360405190815260200160405180910390a35050565b81600160a060020a0316610f9182610906565b600160a060020a031614610fa457600080fd5b600081815260026020526040808220805473ffffffffffffffffffffffffffffffffffffffff19169055600160a060020a038416907f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259084905190815260200160405180910390a35050565b600080600084600160a060020a031661102885610906565b600160a060020a03161461103b57600080fd5b6000848152600460205260409020549250611066600161105a876109e1565b9063ffffffff61124c16565b600160a060020a03861660009081526003602052604090208054919350908390811061108e57fe5b6000918252602080832090910154868352600182526040808420805473ffffffffffffffffffffffffffffffffffffffff19169055600160a060020a03891684526003909252912080549192508291859081106110e757fe5b6000918252602080832090910192909255600160a060020a038716815260039091526040812080548490811061111957fe5b6000918252602080832090910192909255600160a060020a0387168152600390915260409020805490611150906000198301611304565b5060008481526004602052604080822082905582825281208490555461117d90600163ffffffff61124c16565b6000555050505050565b600081815260016020526040812054600160a060020a0316156111a957600080fd5b6000828152600160205260409020805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0385161790556111e6836109e1565b600160a060020a0384166000908152600360205260409020805491925090600181016112128382611304565b50600091825260208083209190910184905583825260049052604081208290555461124490600163ffffffff61125e16565b600055505050565b60008282111561125857fe5b50900390565b60008282018381101561126d57fe5b9392505050565b60206040519081016040526000815290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106112c757805160ff19168380011785556112f4565b828001600101855582156112f4579182015b828111156112f45782518255916020019190600101906112d9565b50611300929150611324565b5090565b815481835581811511610ac857600083815260209020610ac89181019083015b61055c91905b80821115611300576000815560010161132a5600a165627a7a7230582098a49693170ef89b35915882ca7111a15675c8d4c17bdbdabf6e17cee11ffb010029";

    protected GameExchange_sol_GameExchange(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected GameExchange_sol_GameExchange(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<MetadataUpdatedEventResponse> getMetadataUpdatedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("MetadataUpdated", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<MetadataUpdatedEventResponse> responses = new ArrayList<MetadataUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MetadataUpdatedEventResponse typedResponse = new MetadataUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._tokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._newMetadata = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MetadataUpdatedEventResponse> metadataUpdatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("MetadataUpdated", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, MetadataUpdatedEventResponse>() {
            @Override
            public MetadataUpdatedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                MetadataUpdatedEventResponse typedResponse = new MetadataUpdatedEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._tokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._newMetadata = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Approval", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._approved = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Approval", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._approved = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> name() {
        final Function function = new Function("name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _to, BigInteger _tokenId) {
        final Function function = new Function(
                "approve", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function("totalSupply", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> approvedFor(BigInteger _tokenId) {
        final Function function = new Function("approvedFor", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> tokenOfOwnerByIndex(String _owner, BigInteger _index) {
        final Function function = new Function("tokenOfOwnerByIndex", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> optIn(BigInteger _tokenId) {
        final Function function = new Function(
                "optIn", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> burn(BigInteger _tokenId) {
        final Function function = new Function(
                "burn", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> optOut(BigInteger _tokenId) {
        final Function function = new Function(
                "optOut", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setTokenMetadata(BigInteger _tokenId, String _newMetadata) {
        final Function function = new Function(
                "setTokenMetadata", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId), 
                new org.web3j.abi.datatypes.Utf8String(_newMetadata)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<List> tokensOf(String _owner) {
        final Function function = new Function("tokensOf", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<String> ownerOf(BigInteger _tokenId) {
        final Function function = new Function("ownerOf", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> tokenMetadata(BigInteger _tokenId) {
        final Function function = new Function("tokenMetadata", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function("balanceOf", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function("symbol", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> getOptIn(BigInteger _tokenId) {
        final Function function = new Function("getOptIn", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _tokenId) {
        final Function function = new Function(
                "transfer", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> _symbol() {
        final Function function = new Function("_symbol", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> takeOwnership(BigInteger _tokenId) {
        final Function function = new Function(
                "takeOwnership", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> authorize(String _newAuthority) {
        final Function function = new Function(
                "authorize", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_newAuthority)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> mint(String _recipient, String _newMetadata) {
        final Function function = new Function(
                "mint", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_recipient), 
                new org.web3j.abi.datatypes.Utf8String(_newMetadata)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> _name() {
        final Function function = new Function("_name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<GameExchange_sol_GameExchange> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _inName, String _inSymbol, String _authority) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_inName), 
                new org.web3j.abi.datatypes.Utf8String(_inSymbol), 
                new org.web3j.abi.datatypes.Address(_authority)));
        return deployRemoteCall(GameExchange_sol_GameExchange.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<GameExchange_sol_GameExchange> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _inName, String _inSymbol, String _authority) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_inName), 
                new org.web3j.abi.datatypes.Utf8String(_inSymbol), 
                new org.web3j.abi.datatypes.Address(_authority)));
        return deployRemoteCall(GameExchange_sol_GameExchange.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static GameExchange_sol_GameExchange load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new GameExchange_sol_GameExchange(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static GameExchange_sol_GameExchange load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new GameExchange_sol_GameExchange(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class MetadataUpdatedEventResponse {
        public Log log;

        public String _owner;

        public BigInteger _tokenId;

        public String _newMetadata;
    }

    public static class TransferEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _tokenId;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String _owner;

        public String _approved;

        public BigInteger _tokenId;
    }
}
