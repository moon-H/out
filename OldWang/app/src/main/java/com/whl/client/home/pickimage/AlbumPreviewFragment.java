package com.whl.client.home.pickimage;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseFragment;
import com.whl.client.app.CssConstant;

/**
 * Created by liwx on 2017/3/5.
 */
public class AlbumPreviewFragment extends BaseFragment {
    private static final String TAG = "AlbumPreviewFragment";
    private View mRootView;
    private ImageItem mImageItem;
    private Activity mContext;

    public static AlbumPreviewFragment getInstance(ImageItem imageItem) {
        AlbumPreviewFragment f = new AlbumPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CssConstant.BBS.KEY_IMAGE_ITEM, imageItem);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if ((bundle = getArguments()) != null) {
            //            //            forumId = bundle.getInt(CssConstant.BBS.BBS_FORUM_ID);
            //            //            isForumAdmin = bundle.getBoolean(CssConstant.BBS.ISFORUMADMINYN);
            mImageItem = (ImageItem) bundle.getSerializable(CssConstant.BBS.KEY_IMAGE_ITEM);
            MLog.e(TAG, "mTopicCategory = " + mImageItem.toString());
            //        }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MLog.d(TAG, "onCreateView");
        if (mRootView == null) {
            MLog.d(TAG, "newCreateView");
            mRootView = inflater.inflate(R.layout.fragment_album_preview, container, false);
            ImageView imageView = (ImageView) mRootView.findViewById(R.id.img_photo);

            if (mImageItem != null) {
                Glide.with(mContext).load(mImageItem.getImagePath()).into(imageView);
            }

        }

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        super.onDestroyView();
        MLog.d(TAG, "onDestroyView");
    }
}
