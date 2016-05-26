package com.an1metall.businesscard;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class DatePickerDialogFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

    private String uri;

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", getResources().getConfiguration().locale);
        String emailSubject = getString(R.string.invite_to_meeting) + " " + dateFormat.format(calendar.getTime());
        Intent intent = new Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse("mailto:"))
                .putExtra(Intent.EXTRA_EMAIL, new String[]{uri})
                .putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        uri = getArguments().getString("uri");

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, monthOfYear, dayOfMonth);
    }
}
