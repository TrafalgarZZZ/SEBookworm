package com.fxdsse.SEhomework.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxdsse.SEhomework.BMApplication;
import com.fxdsse.SEhomework.BookDetailActivity;
import com.fxdsse.SEhomework.R;
import com.fxdsse.SEhomework.Util.AeolosOnBannerClickListener;
import com.fxdsse.SEhomework.Util.AeolosPicassoImageLoader;
import com.fxdsse.SEhomework.data.BookDetail;
import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.BookDao;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.util.BookDetailDisassembler;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CardView cardView_banner;
    private Banner banner;
    private List<String> images;
    private List<String> titles;
    private List<String> url;
    private LinearLayout homeContainerLinearLayout;
    private BookDao bookDao;
    private DaoSession daoSession;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        daoSession = ((BMApplication) (getActivity().getApplication())).getDaoSession();
        bookDao = daoSession.getBookDao();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardView_banner = (CardView) view.findViewById(R.id.bc_banner_card);
        banner = (Banner) view.findViewById(R.id.bc_banner_banner);
        homeContainerLinearLayout = (LinearLayout) view.findViewById(R.id.home_ll);


        //prepare banner
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l.bottomMargin = 20;
        l.leftMargin = 20;
        l.rightMargin = 20;
        l.topMargin = 40;
        cardView_banner.setLayoutParams(l);
        cardView_banner.setElevation(1);

        images = new ArrayList<>();
        images.add("http://img63.ddimg.cn/upload_img/00087/hw/750x315_dl_20170602.jpg");
        images.add("http://img61.ddimg.cn/upload_img/00570/tongshu/750x315_djj_0602.jpg");
        images.add("http://img61.ddimg.cn/upload_img/00684/zn/dygw750-315.jpg");
        images.add("http://img61.ddimg.cn/upload_img/00713/pictrue/xjpjgs750315.jpg");
        images.add("http://img61.ddimg.cn/upload_img/00570/ta/xiexielikai750x315.jpg");

//        titles = new ArrayList<>();
//        titles.add("儿童节优选图书");
//        titles.add("10万图书 满200减100");
//        titles.add("六一礼品季 儿童节特价活动");
//        titles.add("促销商品推荐");

        url = new ArrayList<>();
        url.add("http://book.dangdang.com/20170602_p3lj");
        url.add("http://baby.dangdang.com/20170602_4hee");
        url.add("http://book.dangdang.com/20170607_pvwk");
        url.add("http://book.dangdang.com/20170607_rkr4");
        url.add("http://book.dangdang.com/20170606_s415");


        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new AeolosPicassoImageLoader());
        banner.setImages(images);
//        banner.setBannerTitles(titles);
        banner.setOnBannerClickListener(new AeolosOnBannerClickListener(getActivity(), url));
        banner.isAutoPlay(true);
        banner.setDelayTime(3000);
        banner.start();

        List<Book> newBookList = bookDao.queryBuilder().list();
        for (Book book : newBookList) {
            RelativeLayout bookItem = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.book_item, null);
            bookItem.setTag(book.getId());
            ImageView imgBook = (ImageView) bookItem.findViewById(R.id.book_pic);
            TextView txtName = (TextView) bookItem.findViewById(R.id.book_name);
            TextView txtPress = (TextView) bookItem.findViewById(R.id.book_press);
            TextView txtPrice = (TextView) bookItem.findViewById(R.id.book_price);
            TextView txtAuthor = (TextView) bookItem.findViewById(R.id.book_author);

            BookDetail detail = BookDetailDisassembler.disassembleDetail(book.getDetail());

            Picasso.with(getActivity()).load(book.getImageURL()).resize(450, 650).centerCrop().into(imgBook);
            txtName.setText(book.getName());
            txtPress.setText(detail.getPress());
            txtPrice.setText(book.getPrice());
            StringBuffer sb = new StringBuffer();
            List<String> authors = detail.getAuthors();
            int authors_size = authors.size();
            for (int i = 0; i < authors_size; i++) {
                sb.append(authors.get(i));
                if (i != authors_size - 1) {
                    sb.append(";");
                }
            }
            txtAuthor.setText(sb);


            bookItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                    intent.putExtra("book_id", (Long) v.getTag());
                    startActivity(intent);
                }
            });

            homeContainerLinearLayout.addView(bookItem);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more informaztion.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
