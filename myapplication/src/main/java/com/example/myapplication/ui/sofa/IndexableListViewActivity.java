package com.example.myapplication.ui.sofa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class IndexableListViewActivity extends Activity {
    private ArrayList<String> mItems;
    private IndexableListView mListView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_han_char_list);

        // 初始化一些数据
        mItems = new ArrayList<String>();
        mItems.add("의 발생");
        mItems.add("초기 검색");
        mItems.add("검색하다");
        mItems.add("당신을 구축");
        mItems.add("두푸");
        mItems.add("하나의");
        mItems.add("현상");
        mItems.add("현상 히히히히히히");
        mItems.add("온라인 상담");
        mItems.add("주문이 있습니다");
        mItems.add("현상");
        mItems.add("컨트리 듀오");
        mItems.add("조금 깨지기 쉬운");
        mItems.add("작은 연약한 것은 동쪽의 붉은");
        mItems.add("선전부 II v");
        mItems.add("특별 예비 II");
        mItems.add("이 제품은 배고픈 사람들");
        mItems.add("연고를 동반하다; 나");
        mItems.add("초등학교는 각각");
        mItems.add("내가 원하는 예술");
        mItems.add("틀리거나 아니면");
        mItems.add("난 ur 들어봐 oh 2 v you");
        mItems.add("고기를 뿌리다");
        mItems.add("Diary of a Wimpy Kid 6: Cabin Fever");
        mItems.add("Steve Jobs");
        mItems.add("Explosive Eighteen: A Stephanie Plum Novel");
        mItems.add("11/22/63: A Novel");
        mItems.add("做作");
        mItems.add("wokao");

        Collections.sort(mItems); // 排序
        ContentAdapter adapter = new ContentAdapter(this, android.R.layout.simple_list_item_1, mItems);

        mListView = (IndexableListView) findViewById(R.id.listview);
        mListView.setAdapter(adapter);
        mListView.setFastScrollEnabled(false); // 设置快速滑动
    }

    private class ContentAdapter extends ArrayAdapter<String> implements
            SectionIndexer {

        private String mSections = "ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎABCDEFGHIJKLMNOPQRSTUVWXYZ#";

        public ContentAdapter(Context context, int textViewResourceId,
                              List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public int getPositionForSection(int sectionIndex) {  /*根据右边索引adbcd...获取左边listView的位置*/
            // If there is no item for current section, previous section will be
            // selected
            // 如果当前部分没有对应item，则之前的部分将被选择  （比如用户点击索引Y，左边list中没有y开头的，则会选择y之前的x,x也没有就找w，一直往前查，直到遇到第一个有对应item的，否则不进行定位）
            for (int i = sectionIndex; i >= 0; i--) {
                for (int j = 0; j < getCount(); j++) {
                    System.out.println(getCount());
                    if (i == 0) { // #
                        // For numeric section 数字
                        for (int k = 0; k <= 9; k++) {// 1...9
                            // 字符串第一个字符与1~9之间的数字进行匹配
                            if (StringMatcher.match(
                                    String.valueOf(getItem(j).charAt(0)),
                                    String.valueOf(k)))
                                return j;
                        }
                    } else { // A~Z
                        if (StringMatcher.match(
                                String.valueOf(getItem(j).charAt(0)),
                                String.valueOf(mSections.charAt(i))))
                            return j;
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Object[] getSections() {
            String[] sections = new String[mSections.length()];
            for (int i = 0; i < mSections.length(); i++)
                sections[i] = String.valueOf(mSections.charAt(i));
            return sections;
        }
    }
}  