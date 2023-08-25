package in.games.Gameskash.Fragment;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
public interface PaymentStatusListener {
    void onTransactionCompleted(TransactionDetails transactionDetails);

    void onTransactionSuccess();

    void onTransactionSubmitted();

    void onTransactionFailed();

    void onTransactionCancelled();

    void onAppNotFound();

    void onPaymentSuccess(String s);

    void onPaymentError(int i, String s);
}
