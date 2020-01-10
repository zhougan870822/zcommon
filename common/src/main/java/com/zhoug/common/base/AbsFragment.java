package com.zhoug.common.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhoug.common.utils.ToastUtils;


/**
 * @Author HK-LJJ
 * @Date 2019/11/28
 * @Description
 */
public abstract class AbsFragment extends Fragment {
    protected static final String TAG = ">>>AbsFragment";

    private static final String STATE_SAVE_IS_HIDDEN = "isHidden";
    protected boolean isDestroyView=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (isHidden) {
                    fragmentTransaction.hide(this);
                } else {
                    fragmentTransaction.show(this);
                }
                fragmentTransaction.commit();
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        init(view);
        isDestroyView=false;
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyView=true;
    }



    protected abstract @LayoutRes
    int getLayoutResId();

    protected abstract void init(View root);

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    //以下是吐司消息
    protected void toastShort(Object msg) {
        if (getActivity() != null) {
            ToastUtils.toastShort(getActivity().getApplicationContext(), msg);
        }
    }

    protected void toastLong(Object msg) {
        if (getActivity() != null) {
            ToastUtils.toastLong(getActivity().getApplicationContext(), msg);
        }
    }

    protected void toastShortCenter(Object msg) {
        if (getActivity() != null) {
            ToastUtils.toastShortCenter(getActivity().getApplicationContext(), msg);
        }
    }

    protected void toastLongCenter(Object msg) {
        if (getActivity() != null) {
            ToastUtils.toastLongCenter(getActivity().getApplicationContext(), msg);
        }
    }

}
