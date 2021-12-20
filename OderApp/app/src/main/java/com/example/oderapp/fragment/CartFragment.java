package com.example.oderapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderapp.R;
import com.example.oderapp.activities.ApiClient;
import com.example.oderapp.activities.PaymentActivity;
import com.example.oderapp.adapters.ItemCartAdappter;
import com.example.oderapp.model.response.ResponseBodyCart;
import com.example.oderapp.utils.Contants;
import com.example.oderapp.utils.StoreUtil;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {
    private RecyclerView mRecyclerView;



    private TextView result;
    private View mview;
    private Button btnCheckOut;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_cart, container, false);
        getCart();
        mRecyclerView = mview.findViewById(R.id.rcv_cart);
        btnCheckOut = mview.findViewById(R.id.btn_check_out);

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                startActivity(intent);
            }
        });
        return mview;
    }

    private void getCart() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Contants.requestToken, "Bearer " + StoreUtil.get(getActivity(), Contants.requestToken));

        Call<ResponseBodyCart> responseDTOCall = ApiClient.getProductService().getCart(hashMap);
        responseDTOCall.enqueue(new Callback<ResponseBodyCart>() {
            @Override
            public void onResponse(Call<ResponseBodyCart> call, Response<ResponseBodyCart> response) {
                ItemCartAdappter adappter = new ItemCartAdappter(getContext(), response.body().getData());
                mRecyclerView.setAdapter(adappter);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
                mRecyclerView.addItemDecoration(itemDecoration);
            }

            @Override
            public void onFailure(Call<ResponseBodyCart> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

