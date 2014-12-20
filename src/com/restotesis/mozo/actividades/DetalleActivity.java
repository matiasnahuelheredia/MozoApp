package com.restotesis.mozo.actividades;

import com.restotesis.mozo.R;
import com.restotesis.mozo.representacion.Eleccion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

public class DetalleActivity extends Activity {

	private NumberPicker pickerCantidad;
	private EditText editNota;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle);
		pickerCantidad = (NumberPicker) findViewById(R.id.numPickCantidad);
		pickerCantidad.setMaxValue(9);
		pickerCantidad.setMinValue(1);
		editNota = (EditText) findViewById(R.id.editTxtNota);
		editNota.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.detalleOK) {
			int cantidad = pickerCantidad.getValue();
			String nota = editNota.getText().toString();
			Bundle bundle = new Bundle();
			Eleccion suEleccion = getIntent().getExtras().getParcelable(
					"suEleccion");
			suEleccion.setCantidad(cantidad);
			suEleccion.setNota(nota);
			bundle.putParcelable("suEleccion", suEleccion);
			Intent intentRegreso = new Intent();
			intentRegreso.putExtras(bundle);
			setResult(RESULT_OK, intentRegreso);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
