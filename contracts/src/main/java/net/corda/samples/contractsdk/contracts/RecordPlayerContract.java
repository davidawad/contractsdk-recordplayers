package net.corda.samples.contractsdk.contracts;

import com.r3.corda.lib.contracts.contractsdk.annotations.RequireNumberOfStatesOnInput;
import com.r3.corda.lib.contracts.contractsdk.annotations.RequireNumberOfStatesOnInputAtLeast;
import com.r3.corda.lib.contracts.contractsdk.annotations.RequireSignersFromEachOutputState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;


// ************
// * Contract *
// ************
public class RecordPlayerContract implements Contract {
    // This id must be used to identify our contract when building a transaction.
    public static final String ID = "net.corda.samples.contractsdk.contracts.RecordPlayerContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {

        final CommandWithParties<RecordPlayerContract.Commands> command = requireSingleCommand(tx.getCommands(), RecordPlayerContract.Commands.class);
        if (command.getValue() instanceof RecordPlayerContract.Commands.Issue) {
        } else {
            // fail verification if we can't find the command
            throw new IllegalArgumentException("Command not found!");
        }

    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {

        // @RequireSignersFromEachOutputState("manufacturer", "dealer")

        @RequireNumberOfStatesOnInput(0)
        class Issue implements Commands {}

        @RequireNumberOfStatesOnInputAtLeast(1)
        class Update implements Commands {}

    }
}

