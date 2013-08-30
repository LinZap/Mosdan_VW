package Internet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import Data.Data;
import android.util.Log;

public class SQL
{

	private final String call_search = "http://" +Data.system_server+"/query.php";
	private HttpPost httpPost;
	private List<NameValuePair> Arg;
	public SQL()
	{
		httpPost = new HttpPost(call_search);
		
	}

	public String[][] getResult(String SQLstring)
	{


		String[][] mar = null;
		Arg = new ArrayList<NameValuePair>();
		Arg.add(new BasicNameValuePair("sqlstr", SQLstring));

		try
		{
			httpPost.setEntity(new UrlEncodedFormEntity(Arg, HTTP.UTF_8));
		
			
			HttpResponse hr = new DefaultHttpClient().execute(httpPost);
			
			if (hr.getStatusLine().getStatusCode() == 200)
			{

				String res = EntityUtils.toString(hr.getEntity());
				Log.d("SQL",res);
				String[] row = res.split("<hr>");

				for (int i = 0; i < row.length; i++)
				{
					
					String[] column = row[i].split("<br>");

					
					if (mar == null) mar = new String[row.length][column.length];
					
					for (int j = 0; j < column.length; j++){mar[i][j] = column[j];
						}					
				}

				
				/*
				for (int i = 0; i < r; i++)
					for (int j = 0; j < c; j++)
						Log.d("("+i+","+j+")", mar[i][j]);
				
				*/
			}

			
		} catch (IOException e)
		{
			e.printStackTrace();
			mar = null;
		}

		return mar;

	}

}
