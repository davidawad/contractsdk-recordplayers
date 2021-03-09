package net.corda.samples.contractsdk.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.TypeOnlyCommandData;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.samples.contractsdk.states.Needle;
import net.corda.samples.contractsdk.states.RecordPlayerState;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.core.TestUtils;
import net.corda.testing.node.MockServices;
import org.junit.Test;

import java.util.Arrays;

import static net.corda.testing.node.NodeTestUtils.ledger;

public class ContractTests {
    private final MockServices ledgerServices = new MockServices();

    // A pre-defined dummy command.
    public interface Commands extends CommandData {
        class DummyCommand extends TypeOnlyCommandData implements Commands{}
    }

    private final Party alice = new TestIdentity(new CordaX500Name("Alice Audio", "", "GB")).getParty();
    private final Party bob = new TestIdentity(new CordaX500Name("Bob's Hustle Records", "", "GB")).getParty();

    @Test
    public void dummyTest() {}

    @Test
    public void mustIncludeIssueCommand() {

        RecordPlayerState st = new RecordPlayerState(alice, bob, Needle.SPHERICAL, 100, 700, 10000, 0, new UniqueIdentifier());

        ledger(ledgerServices, l -> {
            l.transaction(tx -> {
                tx.output(RecordPlayerContract.ID, st);
                tx.command(Arrays.asList(alice.getOwningKey(), bob.getOwningKey()), new Commands.DummyCommand()); // Wrong type!
                return tx.failsWith("Contract verification failed");
            });

            l.transaction(tx -> {
                tx.output(RecordPlayerContract.ID, st);
                tx.command(Arrays.asList(alice.getOwningKey(), bob.getOwningKey()), new RecordPlayerContract.Commands.Issue()); // Correct type
                return tx.verifies();
            });

            return null;
        });

    }



}
