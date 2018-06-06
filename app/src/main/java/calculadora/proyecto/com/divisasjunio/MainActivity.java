package calculadora.proyecto.com.divisasjunio;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;


import static android.util.Log.e;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnPunto, btnDel, btnC, btnDolar, btnEur;
    TextView amount, res;
    public String cantidad = "";
    public String divisa="";
    public String resultado = "";
    private String urlJsonObj = "";

    private static String TAG = MainActivity.class.getSimpleName();
    private Button btnMakeObjectRequest, btnMakeArrayRequest;

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView txtResponse;
    // temporary string to show the parsed response
    private String jsonResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        amount = (TextView) findViewById(R.id.amount);
        res = (TextView) findViewById(R.id.res);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        findViewById(R.id.btn9).setOnClickListener(this);
        findViewById(R.id.btnPunto).setOnClickListener(this);
        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btnC).setOnClickListener(this);
        findViewById(R.id.btnEur).setOnClickListener(this);
        findViewById(R.id.btnDolar).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        String buttonPresionado = ((Button) v).getText().toString();
        switch (buttonPresionado){
            case "C":
                amount.setText("0");
                break;
            case ".":
                if (amount.getText().toString().equals("0")){
                    amount.setText("0"+buttonPresionado);
                }else if (amount.getText().toString().contains(".")){
                    amount.append("");
                }else{
                    amount.append(buttonPresionado);
                }
                break;
            case "EURO":
                divisa = "EUR_USD";
                Conversion();
                break;
            case "DOLAR":
                divisa = "USD_EUR";
                Conversion();
                break;
            default:
                if (amount.getText().toString().startsWith("0") && !amount.getText().toString().startsWith("0.")){
                    amount.setText(""+buttonPresionado);
                }else if (amount.getText().toString().startsWith("0.")){
                    amount.append(buttonPresionado);
                }else{
                    amount.append(buttonPresionado);
                }
                break;
        }
    }
    private void Conversion() {

        urlJsonObj = "http://free.currencyconverterapi.com/api/v5/convert?q="+divisa+"&compact=ultra";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String name = response.getString(divisa);

                    jsonResponse = name;

                    Double cambio=Double.parseDouble(jsonResponse.toString());
                    Double cantidad=Double.parseDouble(amount.getText().toString());
                    System.out.println(cambio*cantidad);
                    Double operacion= Double.parseDouble(jsonResponse.toString())*Double.parseDouble(amount.getText().toString());
                    System.out.println(resultado+2);
                    String resultado = String.valueOf(operacion);
                    res.setText(resultado);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }





}
