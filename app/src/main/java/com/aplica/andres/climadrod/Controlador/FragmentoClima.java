package com.aplica.andres.climadrod.Controlador;

/**
 * Created by andres on 17/11/2017.
 */


import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aplica.andres.climadrod.Modelo.Conexion;
import com.aplica.andres.climadrod.R;
import com.bumptech.glide.Glide;


public class FragmentoClima extends Fragment {


    TextView ciudad;
    TextView ulactualizacion;
    TextView detalles;
    TextView temperatura;
    //TextView icon;
    ImageView imagen;
    Handler handler;

    public FragmentoClima(){
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.clima_fragmento, container, false);
        ciudad= (TextView)rootView.findViewById(R.id.ciudad);
        ulactualizacion = (TextView)rootView.findViewById(R.id.ulactualizacion);
        detalles = (TextView)rootView.findViewById(R.id.detalles);
        temperatura = (TextView)rootView.findViewById(R.id.temperatura);
       // icon = (TextView) rootView.findViewById(R.id.icon);
        imagen=(ImageView) rootView.findViewById(R.id.imagen);


        return rootView;
    }


    private void datosciudad(final String ciudad){
        new Thread(){
            public void run(){
                final JSONObject json = Conexion.getJSON(getActivity(), ciudad);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.no_encontrado),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            infoclima(json);
                        }
                    });

                }
            }
        }.start();
    }


    private void infoclima(JSONObject json){

        try {

            ciudad.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);

            JSONObject windo = json.getJSONObject("wind");
            Glide.with(this).load("http://openweathermap.org/img/w/"+details.getString("icon")+".png").override(300,300).into(imagen);

            int    numerod=windo.getInt("deg");
            String direc="";
            String pus=  details.getString("icon");
            String dizo=pus;

            if(numerod==0)
            {
                direc="NORTE";
            }else if(numerod>0&&numerod<90){
                direc="NORESTE";
            }else if(numerod==90){
                direc="ESTE";
            }else if(numerod>90&&numerod<180){
                direc="SURESTE";
            }else if(numerod==180){
                direc="SUR";
            }else if(numerod>180&&numerod<270){
                direc="SUROESTE";
            }else if(numerod==270){
                direc="OESTE";
            }else if(numerod>270&&numerod<360){
                direc="NOROESTE";
            }
            JSONObject main = json.getJSONObject("main");
            JSONObject wind = json.getJSONObject("wind");
            detalles.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humedad: " + main.getString("humidity") + "%" +
                            "\n" + "Presión: " + main.getString("pressure") + " hPa"+
                            "\n" + "Velocidad Viento: " + wind.getString("speed") + "m/s"+
                            "\n" + "direccion viento: "+ wind.getString("deg") +"°" +
                            "\n"+ direc);

            temperatura.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            ulactualizacion.setText("Ult.información: " + updatedOn);



        }catch(Exception e){
            Log.e("Error", "Uno o más datos no fueron encontrados en los datos JSON");
        }
    }

    public void cambiarCiudad(String ciudad){

        datosciudad(ciudad);
    }


}
