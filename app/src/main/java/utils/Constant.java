package utils;

import com.devrobin.moneytracker.MVVM.Model.CategoryModel;
import com.devrobin.moneytracker.R;

import java.util.ArrayList;

public class Constant {


        public static String INCOME = "Income";
        public static String EXPENSE = "Expense";


    public static ArrayList<CategoryModel> categories;

    public static void setCategories(){

        categories = new ArrayList<>();

        categories.add(new CategoryModel("Salary", R.drawable.food));
        categories.add(new CategoryModel("Business", R.drawable.food));
        categories.add(new CategoryModel("Shopping", R.drawable.food));
        categories.add(new CategoryModel("Bike", R.drawable.food));


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
