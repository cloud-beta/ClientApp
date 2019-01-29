package com.example.user.pushapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.pushapp.microservices.book.BookServiceRetrofitClient;
import com.example.user.pushapp.microservices.book.model.Book;
import com.example.user.pushapp.microservices.book.service.BookService;
import com.example.user.pushapp.microservices.information.InformationServiceRetrofitClient;
import com.example.user.pushapp.microservices.information.model.Restaurant;
import com.example.user.pushapp.microservices.information.service.InformationService;
import com.example.user.pushapp.util.DebugUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookFragment extends Fragment {

    private static final String TAG = "BookFragment";

    private static final String ARG_PLACE_ID = "ARG_PLACE_ID";
    private static final String ARG_PHONE_NUMBER = "ARG_PHONE_NUMBER";

    private Callbacks mCallbacks;
    private CalendarView mCalendarView;
    private EditText mEditText;
    private int mPlaceID;
    private String phoneNumber;

    interface Callbacks {
        void onSubmit();
    }

    /**
     * 프레그먼트가 엑티비티의 프레그먼트 매니저에 등록될때 실행되는 메소드
     * 프레그먼트를 호스팅한 엑티비티를 CallBack 인터페이스로 업캐스팅 후 멥버변수로 등록
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;
            if (a instanceof Callbacks) {
                mCallbacks = (Callbacks) a;
            }
        }

    }

    /**
     * 프레그먼트가 소멸될때 등록 된 콜백 메소드를 해제
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static BookFragment newInstance(String phoneNumber, int placeID) {
        Bundle args = new Bundle();
        args.putInt(ARG_PLACE_ID, placeID);
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        BookFragment fragment = new BookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DebugUtil.logD(TAG, "content load manager clear");
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugUtil.logD(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugUtil.logD(TAG, "Background thread destroyed");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlaceID = getArguments().getInt(ARG_PLACE_ID);
        phoneNumber = getArguments().getString(ARG_PHONE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        mCalendarView = view.findViewById(R.id.calendarView);
        mEditText = view.findViewById(R.id.editText);

        Button submit = view.findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = sdf.format(new Date(BookFragment.this.mCalendarView.getDate()));
                selectedDate += "T" + BookFragment.this.mEditText.getText();

                BookService service = BookServiceRetrofitClient.getInstance().create(BookService.class);
                Call<Void> call = service.bookRestaurant(new Book(phoneNumber, selectedDate, mPlaceID));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        BookFragment.this.mCallbacks.onSubmit();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        DebugUtil.logD(BookFragment.TAG, t.toString());
                        Toast.makeText(BookFragment.this.getContext(), "Fail Book", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

}
