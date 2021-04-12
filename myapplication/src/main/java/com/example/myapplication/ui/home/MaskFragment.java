package com.example.myapplication.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.lib_annotation.FragmentDestination;
import com.example.myapplication.R;
import com.example.myapplication.widget.widget.FloatGuideView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 蒙版 FragmentF
 *
 * @author wangxu
 * @date 2020/9/27
 * @Desc
 */
@FragmentDestination(pageUrl = "main/tabs/home/mask")
public class MaskFragment extends Fragment {


    private List<View> anchorList = new ArrayList<>();
    private FloatGuideView rightScrollGuideView;
    private FloatGuideView singleGuideView;
    private FloatGuideView giftGuideView;
    private FloatGuideView fullGuideView;

    public static MaskFragment newInstance() {
        Bundle args = new Bundle();

        MaskFragment fragment = new MaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 添加锚点 view
     * 1. 社区
     * 2. 礼物
     * 3. 元气满满
     *
     * @param views
     */
    public void setAnchorList(View... views) {
        anchorList.addAll(Arrays.asList(views));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(android.R.layout.activity_list_item, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout[] views = new LinearLayout[4];
        views[2] = new LayoutFullVitalityFloatGuideView(getContext());
        views[1] = new LayoutGiftFloatGuideView(getContext());
        views[0] = new LayoutSingleCommunityFloatGuideView(getContext());
        views[3] = new LayoutRightScrollFloatGuideView(getContext());

        // 默认设置好顺序，外部需要对应传入锚点信息
        rightScrollGuideView = FloatGuideView.Builder.newInstance(getContext())
                .setTargetView(getView())
                .setCustomFloatGuideView(views[3])
//                .setShape(FloatGuideView.MyShape.CIRCULAR)
//                .setDirection(FloatGuideView.Direction.TOP)
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(() -> {
                    rightScrollGuideView.hide();
                    singleGuideView.show();
                })
                .build();
        // 单身社区
        singleGuideView = FloatGuideView.Builder.newInstance(getContext())
                .setTargetView(anchorList.get(0))
                .setCustomFloatGuideView(views[0])
                .setDirection(FloatGuideView.Direction.RIGHT_BOTTOM)
                // 设置圆形显示区域，
                .setShape(FloatGuideView.MyShape.RECTANGULAR)
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(() -> {
                    singleGuideView.hide();
                    giftGuideView.show();
                }).build();

        // 礼物中心
        giftGuideView = FloatGuideView.Builder.newInstance(getContext())
                .setTargetView(anchorList.get(1))
                .setCustomFloatGuideView(views[1])
                .setDirection(FloatGuideView.Direction.LEFT_TOP)
                .setOffset(views[1].getWidth() / 2, 0)
                .setShape(FloatGuideView.MyShape.CIRCULAR)
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(() -> {
                    giftGuideView.hide();
                    fullGuideView.show();
                })
                .build();
        // 元气满满
        fullGuideView = FloatGuideView.Builder.newInstance(getContext())
                .setTargetView(anchorList.get(2))
                .setCustomFloatGuideView(views[2])
                .setDirection(FloatGuideView.Direction.LEFT_TOP)
                .setShape(FloatGuideView.MyShape.RECTANGULAR)
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(() -> {
                    fullGuideView.hide();
                })
                .build();
        rightScrollGuideView.show();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 元气满满
     *
     * @author wangxu
     * @date 2020/9/27
     * @Desc
     */
    private static class LayoutFullVitalityFloatGuideView extends LinearLayout {
        public LayoutFullVitalityFloatGuideView(Context context) {
            this(context, null);
        }

        public LayoutFullVitalityFloatGuideView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public LayoutFullVitalityFloatGuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }


        void init() {
            // 右下方向键
            ImageView direction = new ImageView(this.getContext());
            direction.setImageResource(R.drawable.icon_mask_direction_right_bottom);
//            LayoutParams params = new LayoutParams(144, 100);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            params.topMargin = 24;
            direction.setLayoutParams(params);
            // 文案
            ImageView fullVitality = new ImageView(getContext());
            fullVitality.setImageResource(R.drawable.icon_mask_full_of_vitality);
//            LayoutParams full = new LayoutParams(440, 130);
            LayoutParams full = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            full.rightMargin = 14;
            fullVitality.setLayoutParams(full);

            setOrientation(VERTICAL);
            addView(fullVitality);
            addView(direction);
        }
    }


    /**
     * 礼物中心
     *
     * @author wangxu
     * @date 2020/9/27
     * @Desc
     */
    private static class LayoutGiftFloatGuideView extends LinearLayout {
        public LayoutGiftFloatGuideView(Context context) {
            this(context, null);
        }

        public LayoutGiftFloatGuideView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public LayoutGiftFloatGuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            // 右下方向键
            ImageView direction = new ImageView(this.getContext());
            direction.setImageResource(R.drawable.icon_mask_direction_down);
//            LayoutParams params = new LayoutParams(144, 100);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            params.topMargin = 30;
            direction.setLayoutParams(params);
            // 文案
            ImageView fullVitality = new ImageView(getContext());
            fullVitality.setImageResource(R.drawable.icon_mask_gift_float);
//            LayoutParams full = new LayoutParams(480, 130);
            LayoutParams full = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            fullVitality.setLayoutParams(full);
            setOrientation(VERTICAL);
            addView(fullVitality);
            addView(direction);
        }
    }

    /**
     * 单身社区
     *
     * @author wangxu
     * @date 2020/9/27
     * @Desc
     */
    private static class LayoutSingleCommunityFloatGuideView extends LinearLayout {
        public LayoutSingleCommunityFloatGuideView(Context context) {
            this(context, null);
        }

        public LayoutSingleCommunityFloatGuideView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public LayoutSingleCommunityFloatGuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }


        private void init() {
            // 方向键
            ImageView direction = new ImageView(this.getContext());
            direction.setImageResource(R.drawable.icon_mask_direction_top);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 30;
            direction.setLayoutParams(params);
            // 文案
            ImageView textImage = new ImageView(getContext());
            textImage.setImageResource(R.drawable.icon_mask_single_community);
            LayoutParams full = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textImage.setLayoutParams(full);

            setOrientation(VERTICAL);
            addView(direction);
            addView(textImage);
        }
    }

    /**
     * 单身社区
     *
     * @author wangxu
     * @date 2020/9/27
     * @Desc
     */
    private static class LayoutRightScrollFloatGuideView extends LinearLayout {
        public LayoutRightScrollFloatGuideView(Context context) {
            this(context, null);
        }

        public LayoutRightScrollFloatGuideView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public LayoutRightScrollFloatGuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }


        private void init() {
            // 飞机
            ImageView direction = new ImageView(this.getContext());
            direction.setImageResource(R.drawable.icon_mask_airplan);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            direction.setLayoutParams(params);
            // 文案
            ImageView textImage = new ImageView(getContext());
            textImage.setImageResource(R.drawable.icon_mask_scroll_right);
            LayoutParams full = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textImage.setLayoutParams(full);
            //line
            ImageView line = new ImageView(getContext());
            textImage.setImageResource(R.drawable.icon_mask_scroll_right);
            LayoutParams lineParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            line.setLayoutParams(lineParam);

            setOrientation(VERTICAL);
            addView(direction);
            addView(textImage);
            addView(line);
        }
    }


}


