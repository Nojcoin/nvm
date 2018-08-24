package org.aion.avm.core.testBlake2b;

import org.aion.avm.api.Address;
import org.aion.avm.core.Avm;
import org.aion.avm.core.NodeEnvironment;
import org.aion.avm.core.dappreading.JarBuilder;
import org.aion.avm.core.util.CodeAndArguments;
import org.aion.avm.core.util.Helpers;
import org.aion.kernel.*;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class Blake2bTest {

    private long energyLimit = 10_000_000L;
    private long energyPrice = 1L;
    private Block block = new Block(new byte[32], 1, Helpers.randomBytes(Address.LENGTH), System.currentTimeMillis(), new byte[0]);

    private KernelInterfaceImpl kernel = new KernelInterfaceImpl();
    private Avm avm = NodeEnvironment.singleton.buildAvmInstance(kernel);

    private byte[] deployer = KernelInterfaceImpl.PREMINED_ADDRESS;
    private byte[] dappAddress;

    public Blake2bTest() {
        byte[] jar = JarBuilder.buildJarForMainAndClasses(Main.class, Blake2b.class, Blake2b.Engine.State.class,
                Blake2b.Param.Default.class, Blake2b.Param.Xoff.class, Blake2b.Engine.Assert.class,
                Blake2b.Engine.LittleEndian.class, Blake2b.Engine.flag.class);
        byte[] arguments = null;
        Transaction tx = new Transaction(Transaction.Type.CREATE, deployer, null, 0, new CodeAndArguments(jar, arguments).encodeToBytes(), energyLimit, energyPrice);
        TransactionContext txContext = new TransactionContextImpl(tx, block);
        TransactionResult txResult = avm.run(txContext);
        System.out.println(txResult);

        dappAddress = txResult.getReturnData();
        assertNotNull(dappAddress);
    }

    @Test
    public void testBlake2b() {
        Blake2b mac = Blake2b.Mac.newInstance("key".getBytes());
        byte[] hash = mac.digest("input".getBytes());

        Transaction tx = new Transaction(Transaction.Type.CALL, deployer, dappAddress, 0, new byte[0], energyLimit, energyPrice);
        TransactionContext txContext = new TransactionContextImpl(tx, block);
        TransactionResult txResult = avm.run(txContext);
        System.out.println(txResult);

        assertArrayEquals(hash, txResult.getReturnData());
    }
}
