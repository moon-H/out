//package com.cssweb.shankephone.dialog;
//
//
//import android.app.Dialog;
//import android.content.Context;
//import android.database.Cursor;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.TextView;
//
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.shankephone.BuildConfig;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.gateway.model.ProductCategory;
//import com.cssweb.shankephone.gateway.model.order.ProductStatus;
//import com.cssweb.shankephone.gateway.model.singleticket.CityCode;
//import com.cssweb.shankephone.preference.MPreference;
//
//import org.litepal.crud.DataSupport;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class OrderConditionDialog extends Dialog {
//    private static final String TAG = "OrderConditionDialog";
//
//    private static final int LOCAL_STATUS_CLICKABLE = 1;
//    private static final int LOCAL_STATUS_NOT_CLICKABLE = 2;
//
//    private Context mContext;
//    private OnOrderConditionSelectedListener listener;
//    private LayoutInflater mInflater;
//
//    private GridView mGvProductCategory;
//    private GridView mGvCity;
//    private GridView mGvStatus;
//
//    private CityAdapter mProductAdapter;
//    private CityAdapter mCityAdapter;
//    private StatusAdapter mStatusAdapter;
//
//    private List<ProductCategory> mProductCategoryList = new ArrayList<>();
//    private List<CityCode> mCityCodeArrayList = new ArrayList<>();
//    private List<ProductStatus> mStatusArrayList = new ArrayList<>();
//
//    private List<ProductCategory> mSupportCurrentConditionProductCodeList = new ArrayList<>();
//
//    //    private String mCurrentCity;
//    //    private String mCurrentProductCategoryCode;
//
//    private String mLastSelectCity;
//    private String mLastSelectStatus;
//    private String mLastSelectProductName;
//    private String mLastSelectProductCode;
//
//    private List<String> mSingleTicketStatusStrList = new ArrayList<>();
//    private List<String> mCsMetroStatusStrList = new ArrayList<>();
//
//    private Handler mHandler = new Handler();
//
//    public OrderConditionDialog(Context context) {
//        super(context, R.style.OrderConditionDialogStyle);
//        mContext = context;
//        setContentView(R.layout.dialog_order_condition);
//        initViews(context);
//        //        initData();
//    }
//
//    private void initViews(Context context) {
//        //        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        //        titleBarView.setTitle(context.getString(R.string.order));
//        findViewById(R.id.lly_transparent).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//        Window window = this.getWindow();
//        if (window != null) {
//            window.setGravity(Gravity.TOP);
//            WindowManager.LayoutParams layoutParams = window.getAttributes();
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            layoutParams.width = params.width; // 宽度
//            layoutParams.height = params.height; // 高度
//            //            layoutParams.x = 0;
//            //            layoutParams.y = context.getResources().getDimensionPixelOffset(R.dimen.title_bar_height) + context.getResources().getDimensionPixelOffset(R.dimen.title_bar_height);
//            window.setWindowAnimations(R.style.OrderDialogWindowAnim);
//            //            window.setBackgroundDrawableResource(R.color.transparent);
//            window.setAttributes(layoutParams);
//
//            //            ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, DeviceInfo.getScreenWidth(context), 0);
//            //            scaleAnimation.setDuration(500);
//
//            mGvProductCategory = (GridView) findViewById(R.id.gv_product);
//            mGvCity = (GridView) findViewById(R.id.gv_city);
//            mGvStatus = (GridView) findViewById(R.id.gv_status);
//
//            mProductAdapter = new CityAdapter();
//            mGvProductCategory.setAdapter(mProductAdapter);
//
//            mCityAdapter = new CityAdapter();
//            mGvCity.setAdapter(mCityAdapter);
//
//            mStatusAdapter = new StatusAdapter();
//            mGvStatus.setAdapter(mStatusAdapter);
//
//            findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                    ProductCategory productCategory = mProductAdapter.getItem(mProductAdapter.getSelectedPosition());
//                    CityCode cityCode = mCityAdapter.getItem(mCityAdapter.getCurrentPosition());
//                    ProductStatus status = mStatusAdapter.getItem(mStatusAdapter.getCurrentPosition());
//                    if (listener != null) {
//                        listener.onOrderConditionSelected(productCategory, cityCode, status);
//                    }
//
//                }
//            });
//
//            mGvProductCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    //                    CommonToast.toast(mContext, "点击了");
//
//                    if (position == mProductAdapter.getSelectedPosition()) {
//                        MLog.d(TAG, "onItemClick current item has selected");
//                        return;
//                    }
//                    boolean isCurrentClickable = false;
//                    String clickProductCode = mProductAdapter.getItem(position).getCategoryCode();
//                    MLog.d(TAG, "clickProductCode = " + clickProductCode);
//                    for (ProductCategory productCategory : mSupportCurrentConditionProductCodeList) {
//                        if (productCategory.getCategoryCode().equals(clickProductCode)) {
//                            isCurrentClickable = true;
//                            break;
//                        }
//                    }
//                    if (isCurrentClickable) {
//                        //                        mSupportCurrentConditionProductCodeList.clear();
//                        mLastSelectProductName = mProductAdapter.getItem(position).getCategoryName();
//                        mProductAdapter.setSelectedPosition(position);
//                        mLastSelectProductCode = mProductAdapter.getItem(position).getCategoryCode();
//                        //选择商品后刷新城市和状态
//                        updateProductCity(mLastSelectProductCode);
//                        updateStatus(mLastSelectProductCode);
//                        MLog.d(TAG, "lastCity = " + mLastSelectCity + " lastStatus = " + mLastSelectStatus);
//                        mCityAdapter.setSelectedName(mLastSelectCity);
//                        mStatusAdapter.setSelectedStatus(mLastSelectStatus);
//                    } else {
//                        //                        CommonToast.toast(BizApplication.getInstance(), "当前商品不可点击");
//                    }
//
//
//                }
//            });
//
//            mGvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    if (position == mCityAdapter.getCurrentPosition()) {
//                        MLog.d(TAG, "onItemClick current item has selected");
//                        return;
//                    }
//                    mLastSelectCity = mCityAdapter.getItem(position).getCityName();
//                    mCityAdapter.setSelectedName(mLastSelectCity);
//                    //选择城市后刷新商品列表
//                    String cityCode = mCityCodeArrayList.get(position).getCityCode();
//                    String status = mStatusAdapter.getItem(mStatusAdapter.getCurrentPosition()).getStatusName();
//                    //                    MLog.d(TAG, "## status = " + status + " cityCode = " + cityCode);
//                    updateProductListByCondition(cityCode, status);
//
//                }
//
//            });
//
//            mGvStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    //选择状态后刷新商品
//                    if (position == mStatusAdapter.getCurrentPosition()) {
//                        MLog.d(TAG, "onItemClick current item has selected");
//                        return;
//                    }
//                    mLastSelectStatus = mStatusAdapter.getItem(position).getStatusName();
//                    mStatusAdapter.setCurrentPosition(position);
//                    mStatusAdapter.setSelectedStatus(mLastSelectStatus);
//                    //                    ProductStatus status = mStatusArrayList.get(position);
//                    String cityCode = mCityAdapter.getItem(mCityAdapter.getCurrentPosition()).getCityCode();
//                    updateProductListByCondition(cityCode, mLastSelectStatus);
//                    //                    MLog.d(TAG, "## status = " + status.toString() + " cityCode = " + cityCode);
//
//                    //                    updateProdu ctByStatus(getSingleTicketStatusStrByProductCode(status));
//                }
//            });
//        }
//    }
//
//    public void setDefaultProductCategory(String productCategoryCode) {
//        mLastSelectProductCode = productCategoryCode;
//    }
//
//    public void updateCondition(String productCode, String productName, final String cityCode, String cityName, String status) {
//        //        mCurrentCity = cityCode;
//        MLog.d(TAG, "## productCode = " + productCode + " productName = " + productName + " cityCode = " + cityCode + " cityName = " + cityName + " status = " + status);
//        mProductCategoryList.clear();
//        mLastSelectCity = cityName;
//        mLastSelectStatus = status;
//        mLastSelectProductName = productName;
//        mLastSelectProductCode = productCode;
//
//        BizApplication.getInstance().getThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                String choiceCity = MPreference.getString(BizApplication.getInstance(), MPreference.CHOICE_CITY_CODE);
//
//                List<ProductCategory> productCategoryList = BizApplication.getInstance().getCommonDBManager().getAllProduct();
//                if (productCategoryList != null) {
//                    for (int i = 0; i < productCategoryList.size(); i++) {
//                        boolean isSupportCurrentCity = false;
//                        ProductCategory productCategory = productCategoryList.get(i);
//                        List<CityCode> list = productCategory.getCitys();
//                        for (CityCode city : list) {
//                            if (city.getCityCode().equals(choiceCity)) {
//                                isSupportCurrentCity = true;
//                            }
//                        }
//                        if (isSupportCurrentCity) {
//                            mProductCategoryList.add(productCategory);
//                        }
//                    }
//                }
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //                        mProductAdapter.notifyDataSetChanged();
//                        updateProductCity(mLastSelectProductCode);
//                        updateStatus(mLastSelectProductCode);
//                        updateProduct();
//                    }
//                });
//            }
//        });
//    }
//
//    private void updateProductCity(String productCode) {
//        if (!TextUtils.isEmpty(productCode)) {
//            mCityCodeArrayList.clear();
//            for (ProductCategory productCategory : mProductCategoryList) {
//                if (productCode.equalsIgnoreCase(productCategory.getCategoryCode())) {
//                    List<CityCode> list = productCategory.getCitys();
//                    if (list != null) {
//                        mCityCodeArrayList.addAll(list);
//                    }
//                    break;
//                }
//            }
//            mCityAdapter.setSelectedName(mLastSelectCity);
//            //            mCityAdapter.notifyDataSetChanged();
//        } else {
//            MLog.d(TAG, "getProductCityList productCode is null");
//        }
//    }
//
//    private void updateProduct() {
//        if (mCityAdapter.getCount() > 0 && mStatusAdapter.getCount() > 0) {
//            int currentPosition = mCityAdapter.getCurrentPosition();
//            if (currentPosition != -1) {
//                String cityCode = mCityAdapter.getItem(currentPosition).getCityCode();
//                String statusName = mStatusAdapter.getSelectedName();
//                updateProductListByCondition(cityCode, statusName);
//            } else {
//                MLog.d(TAG, "currentPosition is -1");
//            }
//        } else {
//            MLog.d(TAG, "CITY IS NULL");
//        }
//    }
//
//    private void updateStatus(String productCode) {
//        if (!TextUtils.isEmpty(productCode)) {
//            mStatusArrayList.clear();
//            for (ProductCategory productCategory : mProductCategoryList) {
//                if (productCode.equalsIgnoreCase(productCategory.getCategoryCode())) {
//                    List<ProductStatus> list = productCategory.getStatus();
//                    if (list != null) {
//                        mStatusArrayList.addAll(list);
//                    }
//                    break;
//                }
//            }
//            mStatusAdapter.setSelectedStatus(mLastSelectStatus);
//            //            mStatusAdapter.notifyDataSetChanged();
//        } else {
//            MLog.d(TAG, "getStatusList productCode is null");
//        }
//    }
//
//    public void setOnOrderConditionSelectedListener(OnOrderConditionSelectedListener listener) {
//        this.listener = listener;
//    }
//
//    public interface OnOrderConditionSelectedListener {
//        void onOrderConditionSelected(ProductCategory productCategory, CityCode cityCode, ProductStatus productStatus);
//    }
//
//
//    private class CityAdapter extends BaseAdapter {
//        int selectedPosition = 0;
//
//        void setSelectedPosition(int position) {
//            selectedPosition = position;
//            notifyDataSetChanged();
//        }
//
//        void setSelectedName(String name) {
//            setSelectedPosition(getPositionByName(name));
//            notifyDataSetChanged();
//        }
//
//        private int getPositionByName(String productName) {
//            for (int i = 0; i < mProductCategoryList.size(); i++) {
//                ProductCategory productCategory = mProductCategoryList.get(i);
//                if (productCategory.getCategoryName().equals(productName)) {
//                    return i;
//                }
//            }
//            return -1;
//        }
//
//        int getSelectedPosition() {
//            return selectedPosition;
//        }
//
//        @Override
//        public int getCount() {
//            return mProductCategoryList.size();
//        }
//
//        @Override
//        public ProductCategory getItem(int position) {
//            return mProductCategoryList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            ProductCategory item = mProductCategoryList.get(position);
//            //            MLog.d(TAG, " Item productCode = " + item.getCategoryCode() + " productName = " + item.getCategoryName());
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = View.inflate(mContext, R.layout.item_order_dialog_list, null);
//                holder.product = (TextView) convertView.findViewById(R.id.tv_item);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            if (selectedPosition == position) {
//                holder.product.setSelected(true);
//                holder.product.setTextColor(mContext.getResources().getColor(R.color.FFFFFF));
//            } else {
//                holder.product.setTextColor(mContext.getResources().getColor(R.color._333333));
//                holder.product.setSelected(false);
//                if (item.getLocalStatus() == LOCAL_STATUS_CLICKABLE) {
//                    holder.product.setTextColor(mContext.getResources().getColor(R.color._333333));
//                } else if (item.getLocalStatus() == LOCAL_STATUS_NOT_CLICKABLE) {
//                    holder.product.setTextColor(mContext.getResources().getColor(R.color.CCCCCC));
//                }
//                //                for (ProductCategory productCategory : mSupportCurrentConditionProductCodeList) {
//                //                    if (productCategory.getCategoryCode().equals(item.getCategoryCode())) {
//                //                    } else {
//                //                        MLog.d(TAG, " getView productCode = " + productCategory.getCategoryCode() + " productName = " + productCategory.getCategoryName());
//                //                    }
//                //                }
//            }
//
//            holder.product.setText(item.getCategoryName());
//            return convertView;
//        }
//
//        class ViewHolder {
//            TextView product;
//        }
//
//    }
//
//    private class CityAdapter extends BaseAdapter {
//        //        String selectedCityName = "";
//        int currentPosition = 0;
//
//        int getCurrentPosition() {
//            return currentPosition;
//        }
//
//        public void setCurrentPosition(int currentPosition) {
//            this.currentPosition = currentPosition;
//        }
//
//        void setSelectedName(String name) {
//            //            selectedCityName = name;
//            setCurrentPosition(getPositionByName(name));
//            notifyDataSetChanged();
//        }
//
//        private int getPositionByName(String cityName) {
//            for (int i = 0; i < mCityCodeArrayList.size(); i++) {
//                CityCode cityCode = mCityCodeArrayList.get(i);
//                if (cityCode.getCityName().equals(cityName)) {
//                    return i;
//                }
//            }
//            return 0;
//        }
//
//        String getSelectedName() {
//            return mCityCodeArrayList.get(currentPosition).getCityName();
//        }
//
//        @Override
//        public int getCount() {
//            return mCityCodeArrayList.size();
//        }
//
//        @Override
//        public CityCode getItem(int position) {
//            return mCityCodeArrayList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            CityCode item = mCityCodeArrayList.get(position);
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = View.inflate(mContext, R.layout.item_order_dialog_list, null);
//                holder.cityCode = (TextView) convertView.findViewById(R.id.tv_item);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            if (currentPosition == position) {
//                holder.cityCode.setSelected(true);
//                holder.cityCode.setTextColor(mContext.getResources().getColor(R.color.FFFFFF));
//            } else {
//                holder.cityCode.setTextColor(mContext.getResources().getColor(R.color._333333));
//                holder.cityCode.setSelected(false);
//            }
//            holder.cityCode.setText(item.getCityName());
//            return convertView;
//        }
//
//        class ViewHolder {
//            TextView cityCode;
//        }
//
//    }
//
//    private class StatusAdapter extends BaseAdapter {
//        //        String selectedStatusName = "";
//        int currentPosition = 0;
//
//        public int getCurrentPosition() {
//            return currentPosition;
//        }
//
//        public void setCurrentPosition(int currentPosition) {
//            this.currentPosition = currentPosition;
//        }
//
//        void setSelectedStatus(String statusName) {
//            //            selectedStatusName = statusName;
//            setCurrentPosition(getPositionByName(statusName));
//            notifyDataSetChanged();
//        }
//
//        private int getPositionByName(String statusName) {
//            for (int i = 0; i < mStatusArrayList.size(); i++) {
//                ProductStatus productStatus = mStatusArrayList.get(i);
//                if (productStatus.getStatusName().equals(statusName)) {
//                    return i;
//                }
//            }
//            return -1;
//        }
//
//        String getSelectedName() {
//            MLog.d(TAG, "StatusAdapter position = " + currentPosition);
//            if (mStatusArrayList.size() > 0) {
//                if (currentPosition != -1)
//                    return mStatusArrayList.get(currentPosition).getStatusName();
//            }
//            return "";
//        }
//
//        @Override
//        public int getCount() {
//            return mStatusArrayList.size();
//        }
//
//        @Override
//        public ProductStatus getItem(int position) {
//            return mStatusArrayList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            ProductStatus status = mStatusArrayList.get(position);
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = View.inflate(mContext, R.layout.item_order_dialog_list, null);
//                holder.status = (TextView) convertView.findViewById(R.id.tv_item);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            if (currentPosition == position) {
//                holder.status.setSelected(true);
//                holder.status.setTextColor(mContext.getResources().getColor(R.color.FFFFFF));
//            } else if (currentPosition == -1 && position == 0) {
//                holder.status.setSelected(true);
//                holder.status.setTextColor(mContext.getResources().getColor(R.color.FFFFFF));
//            } else {
//                holder.status.setTextColor(mContext.getResources().getColor(R.color._333333));
//                holder.status.setSelected(false);
//            }
//
//            holder.status.setText(status.getStatusName());
//            return convertView;
//        }
//
//        class ViewHolder {
//            TextView status;
//        }
//
//    }
//
//    //    private void getProductListByCondition(String cityCode, String statusName) {
//    //        Cursor cursor = DataSupport.findBySQL("select * from ProductCategory,CityCode,ProductStatus where ProductCategory.id = CityCode.productcategory_id and cityCode = ?", "4401");
//    //        while (cursor.moveToNext()) {
//    //            MLog.d(TAG, cursor.getString(cursor.getColumnIndex("categoryname")));
//    //        }
//    //    }
//    private void updateProductListByCondition(final String cityCode, final String statusName) {
//        BizApplication.getInstance().getThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                MLog.d(TAG, "### updateProductListByCondition cityCode = " + cityCode + "  statusName = " + statusName);
//                mSupportCurrentConditionProductCodeList.clear();
//                ProductCategory productCategory;
//                Cursor cursor = DataSupport.findBySQL("select * from ProductCategory INNER JOIN CityCode ON ProductCategory.id = CityCode.productcategory_id INNER JOIN PRODUCTSTATUS ON ProductCategory.id = PRODUCTSTATUS.productcategory_id where CityCode.cityCode = ? and PRODUCTSTATUS.statusname = ?", cityCode, statusName);
//                if (cursor != null) {
//                    while (cursor.moveToNext()) {
//                        productCategory = new ProductCategory();
//                        productCategory.setCategoryName(cursor.getString(cursor.getColumnIndex("categoryname")));
//                        productCategory.setCategoryCode(cursor.getString(cursor.getColumnIndex("categorycode")));
//                        mSupportCurrentConditionProductCodeList.add(productCategory);
//                        MLog.d(TAG, "### " + productCategory.toString());
//                    }
//                    cursor.close();
//                } else {
//                    MLog.d(TAG, "updateProductListByCondition cursor is null");
//                }
//                if (BuildConfig.LOG_DEBUG) {
//                    for (int i = 0; i < mSupportCurrentConditionProductCodeList.size(); i++) {
//                        ProductCategory item = mSupportCurrentConditionProductCodeList.get(i);
//                        MLog.d(TAG, " query productCode = " + item.getCategoryCode() + " productName = " + item.getCategoryName());
//                    }
//                }
//                for (int i = 0; i < mProductCategoryList.size(); i++) {
//                    ProductCategory category = mProductCategoryList.get(i);
//                    boolean clickeAble = false;
//                    for (ProductCategory product : mSupportCurrentConditionProductCodeList) {
//                        if (category.getCategoryCode().equals(product.getCategoryCode())) {
//                            clickeAble = true;
//                            //                            category.setLocalStatus(LOCAL_STATUS_CLICKABLE);
//                        }
//                    }
//                    if (clickeAble) {
//                        category.setLocalStatus(LOCAL_STATUS_CLICKABLE);
//                    } else {
//                        category.setLocalStatus(LOCAL_STATUS_NOT_CLICKABLE);
//                    }
//                }
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mProductAdapter.setSelectedName(mLastSelectProductName);
//                        //                        mProductAdapter.setSelectedPosition();
//                        //                        mProductAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        });
//    }
//
//}
