package utils;

import com.devrobin.moneytracker.MVVM.Model.DateHeaderModel;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;

public class ListItem {

    public static final int typeHeader = 0;
    public static final int typeTransaction = 1;

    private int type;
    private DateHeaderModel header;
    private TransactionModel transModel;

    public ListItem(DateHeaderModel header){
        this.type = typeHeader;
        this.header = header;
    }

    public ListItem(TransactionModel transModel){
        this.type = typeTransaction;
        this.transModel = transModel;
    }

    public int getType() {
        return type;
    }

    public DateHeaderModel getHeader() {
        return header;
    }

    public TransactionModel getTransModel() {
        return transModel;
    }
}
