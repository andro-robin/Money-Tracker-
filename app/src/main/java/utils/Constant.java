package utils;

import com.devrobin.moneytracker.MVVM.Model.CategoryModel;
import com.devrobin.moneytracker.R;

import java.util.ArrayList;

public class Constant {


        public static String INCOME = "Income";
        public static String EXPENSE = "Expense";


    public static ArrayList<CategoryModel> categories;


    public static int DAILY = 0;
    public static int MONTHLY = 1;
    public static int CALENDAR = 2;
    public static int SUMMARY = 3;

    public static void setCategories(){

        categories = new ArrayList<>();

        categories.add(new CategoryModel("Salary", R.drawable.food));
        categories.add(new CategoryModel("Business", R.drawable.investment));
        categories.add(new CategoryModel("Shopping", R.drawable.shopping));
        categories.add(new CategoryModel("Bike", R.drawable.bike));
        categories.add(new CategoryModel("Travel", R.drawable.travel));
        categories.add(new CategoryModel("Rent", R.drawable.rent));
        categories.add(new CategoryModel("Electronics", R.drawable.electronics));
        categories.add(new CategoryModel("Clothing", R.drawable.clothing));
        categories.add(new CategoryModel("Health", R.drawable.health));
        categories.add(new CategoryModel("Pet", R.drawable.pet));
        categories.add(new CategoryModel("Gifts", R.drawable.gift));
        categories.add(new CategoryModel("Phone", R.drawable.phone));
        categories.add(new CategoryModel("Beauty", R.drawable.beauty));
        categories.add(new CategoryModel("Social", R.drawable.social));
        categories.add(new CategoryModel("Sport", R.drawable.sports));
        categories.add(new CategoryModel("Housing", R.drawable.house));
        categories.add(new CategoryModel("Market", R.drawable.market));
        categories.add(new CategoryModel("Others", R.drawable.others));



    }


    public static CategoryModel setCategoryDetails(String categoryName){

        for (CategoryModel cat:
             categories) {
            if (cat.getCategoryName().equals(categoryName)){
                return cat;
            }
        }

        return null;
    }

}
