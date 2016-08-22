package org.interledger.ilp.core.events;

import org.interledger.ilp.core.InterledgerPacketHeader;
import org.interledger.ilp.core.Ledger;

public abstract class LedgerTransferEvent extends LedgerEvent {

    private static final long serialVersionUID = -5898667111660754487L;

    protected InterledgerPacketHeader ilpPacketHeader;
    protected String localFromAccount;
    protected String localToAccount;
    protected String localTransferAmount;

    public LedgerTransferEvent(Ledger source, InterledgerPacketHeader ilpPacketHeader,
            String localFromAccount, String localToAccount, String localTransferAmount) {
        super(source);

        this.ilpPacketHeader = ilpPacketHeader;
        this.localFromAccount = localFromAccount;
        this.localToAccount = localToAccount;
        this.localTransferAmount = localTransferAmount;
    }

    public InterledgerPacketHeader getInterledgerPacketHeader() {
        return ilpPacketHeader;
    }

    public String getLocalFromAccount() {
        return localFromAccount;
    }

    public String getLocalToAccount() {
        return localToAccount;
    }

    public String getLocalTransferAmount() {
        return localTransferAmount;
    }

}
