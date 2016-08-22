package org.interledger.ilp.core.events;

import org.interledger.ilp.core.InterledgerPacketHeader;
import org.interledger.ilp.core.Ledger;

public class LedgerTransferExecutedEvent extends LedgerTransferEvent {

    private static final long serialVersionUID = 2742406317777118624L;

    public LedgerTransferExecutedEvent(Ledger source,
            InterledgerPacketHeader ilpPacketHeader, String localFromAccount,
            String localToAccount, String localTransferAmount) {

        super(source, ilpPacketHeader, localFromAccount, localToAccount,
                localTransferAmount);

    }

}
