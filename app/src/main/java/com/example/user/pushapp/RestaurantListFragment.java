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

import com.example.user.pushapp.microservices.information.InformationServiceRetrofitClient;
import com.example.user.pushapp.microservices.information.model.Restaurant;
import com.example.user.pushapp.microservices.information.service.InformationService;
import com.example.user.pushapp.util.DebugUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListFragment extends Fragment {

    private static final String TAG = "RestaurantListFragment";

    private RestaurantAdapter mRestaurantAdapter;
    private RecyclerView mRestaurantRecyclerView;

    private MainActivity mActivity;


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
            if (a instanceof MainActivity) {
                mActivity = (MainActivity) a;
            }
        }

    }

    /**
     * 프레그먼트가 소멸될때 등록 된 콜백 메소드를 해제
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    public static RestaurantListFragment newInstance() {
        Bundle args = new Bundle();
        RestaurantListFragment fragment = new RestaurantListFragment();
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
        updateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugUtil.logD(TAG, "Background thread destroyed");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        mRestaurantRecyclerView = (RecyclerView) view
                .findViewById(R.id.restaurant_recycler_view);

        mRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRestaurantAdapter = new RestaurantAdapter(mActivity);
        mRestaurantRecyclerView.setAdapter(mRestaurantAdapter);

        return view;
    }

    public void updateUI() {

        InformationService service = InformationServiceRetrofitClient.getInstance().create(InformationService.class);
        Call<List<Restaurant>> call = service.listRestaurant();
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                mRestaurantAdapter.changeContents(response.body());
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {

            }
        });

    }


}
