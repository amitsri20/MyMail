package amit.mymails.Api;

import java.util.List;

import amit.mymails.model.Email;
import amit.mymails.model.EmailDetails;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static android.os.FileObserver.DELETE;


/**
 * Created by amit on 9/25/2016.
 */

public interface WebyogApi {
    @GET("api/message/")
    Call<List<Email>> loadItems();
    @GET("api/message/{id}")
    Call<EmailDetails> getEmailDetails(@Path("id") String emailId);
    @DELETE("api/message/{id}")
    Response deleteItem(@Path("id") String itemId);
}
