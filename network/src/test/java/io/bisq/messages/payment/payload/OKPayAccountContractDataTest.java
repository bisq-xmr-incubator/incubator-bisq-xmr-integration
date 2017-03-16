package io.bisq.messages.payment.payload;

import com.google.protobuf.util.JsonFormat;
import io.bisq.common.wire.proto.Messages;
import io.bisq.network_messages.payment.payload.OKPayAccountContractData;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

/**
 * Created by mike on 27/02/2017.
 */
public class OKPayAccountContractDataTest {
    @Test
    public void toProtoBuf() throws Exception {
        OKPayAccountContractData accountContractData = new OKPayAccountContractData("method", "id", 100);

        try {
            String buffer = JsonFormat.printer().print(accountContractData.toProtoBuf().getOKPayAccountContractData());
            JsonFormat.Parser parser = JsonFormat.parser();
            Messages.OKPayAccountContractData.Builder builder = Messages.OKPayAccountContractData.newBuilder();
            parser.merge(buffer, builder);
            //assertEquals(accountContractData, new OKPayAccountContractData()ProtoBufferUtilities.getOkPayAccountContractData(builder.build()));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

}