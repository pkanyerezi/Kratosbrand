package com.brand.Kratos;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.Kratos.adapter.MediumAdapter;
import com.brand.Kratos.adapter.RoundAdapter;
import com.brand.Kratos.adapter.SmallAdapter;
import com.brand.Kratos.model.HomeModel;

import java.util.ArrayList;

//activity_main
public class MainActivity extends AppCompatActivity {

    TextView txt;
    ImageView search;
    SharedPreferences pref;

    private ArrayList<HomeModel> homeListModelClassArrayList1;
    private RecyclerView menuRecycler;
    private SmallAdapter bAdapter;

    private ArrayList<HomeModel> homeListModelClassArrayList2;
    private RecyclerView recyclerView2;
    private MediumAdapter aAdapter;

    private ArrayList<HomeModel> homeListModelClassArrayList3;
    private RecyclerView recyclerView3;
    private MediumAdapter cAdapter;

    private ArrayList<HomeModel> homeListModelClassArrayList4;
    private RecyclerView recyclerView4;
    private MediumAdapter dAdapter;

    private ArrayList<HomeModel> homeListModelClassArrayList5;
    private RecyclerView recyclerView5;
    private RoundAdapter eAdapter;

    private ArrayList<HomeModel> homeListModelClassArrayList6;
    private RecyclerView recyclerView6;
    private MediumAdapter fAdapter;


    String name []={"Westlife","Love story","Laila main\n" +
            "laila","High End","Dum mar\n" +
            "o dum","I love you","lonely","Westlife"};
    Integer image[]={R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background
            ,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt=(TextView)findViewById(R.id.txt);
        search=(ImageView)findViewById(R.id.search);

        txt.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);


        //////////////
        //horizontal Recycler for recently played list...........
        menuRecycler = findViewById(R.id.recycler1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        menuRecycler.setLayoutManager(layoutManager);
        menuRecycler.setItemAnimator(new DefaultItemAnimator());

        homeListModelClassArrayList1 = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            HomeModel beanClassForRecyclerView_contacts = new HomeModel(name[i],image[i]);
            homeListModelClassArrayList1.add(beanClassForRecyclerView_contacts);
        }
        bAdapter = new SmallAdapter(MainActivity.this,homeListModelClassArrayList1);
        menuRecycler.setAdapter(bAdapter);


        /////////////////////////
        //Horizonatl recycler for popula album list with ssame model class like that of above recycler...........
        recyclerView2 = findViewById(R.id.recycler2);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView2.setLayoutManager(mlayoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        homeListModelClassArrayList2 = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            HomeModel beanClassForRecyclerView_contacts = new HomeModel(name[i],image[i]);
            homeListModelClassArrayList2.add(beanClassForRecyclerView_contacts);
        }
        aAdapter = new MediumAdapter(MainActivity.this,homeListModelClassArrayList2);
        recyclerView2.setAdapter(aAdapter);


        ////////////
        //Recycler with same adapgter and model class as above recycler....
        recyclerView3 = findViewById(R.id.recycler3);
        RecyclerView.LayoutManager clayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView3.setLayoutManager(clayoutManager);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());

        homeListModelClassArrayList3 = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            HomeModel beanClassForRecyclerView_contacts = new HomeModel(name[i],image[i]);
            homeListModelClassArrayList3.add(beanClassForRecyclerView_contacts);
        }
        cAdapter = new MediumAdapter(MainActivity.this,homeListModelClassArrayList3);
        recyclerView3.setAdapter(cAdapter);


        ////////////
        //Recycler with same adapgter and model class as above recycler....
        recyclerView4 = findViewById(R.id.recycler4);
        RecyclerView.LayoutManager dlayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView4.setLayoutManager(dlayoutManager);
        recyclerView4.setItemAnimator(new DefaultItemAnimator());

        homeListModelClassArrayList4 = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            HomeModel beanClassForRecyclerView_contacts = new HomeModel(name[i],image[i]);
            homeListModelClassArrayList4.add(beanClassForRecyclerView_contacts);
        }
        dAdapter = new MediumAdapter(MainActivity.this,homeListModelClassArrayList4);
        recyclerView4.setAdapter(dAdapter);

        ////////////////
        //circular view with horizontal Recycler for the Genres list with same model as above......
        recyclerView5 = findViewById(R.id.recycler5);
        RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView5.setLayoutManager(elayoutManager);
        recyclerView5.setItemAnimator(new DefaultItemAnimator());

        homeListModelClassArrayList5 = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            HomeModel beanClassForRecyclerView_contacts = new HomeModel(name[i],image[i]);
            homeListModelClassArrayList5.add(beanClassForRecyclerView_contacts);
        }
      /*  eAdapter = new RoundAdapter(MainActivity.this,homeListModelClassArrayList5);
        recyclerView5.setAdapter(eAdapter);*/


        ////////////////
        //circular view with horizontal Recycler for the Genres list with same model as above......
        recyclerView6 = findViewById(R.id.recycler6);
        RecyclerView.LayoutManager flayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView6.setLayoutManager(flayoutManager);
        recyclerView6.setItemAnimator(new DefaultItemAnimator());

        homeListModelClassArrayList6 = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            HomeModel beanClassForRecyclerView_contacts = new HomeModel(name[i],image[i]);
            homeListModelClassArrayList6.add(beanClassForRecyclerView_contacts);
        }
      /*  fAdapter = new MediumAdapter(MainActivity.this,homeListModelClassArrayList6);
        recyclerView6.setAdapter(fAdapter);*/

    }
}
