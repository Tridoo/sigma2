package tridoo.sigma2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

public class Repository {

    private final String EMPTY_JSON = "{}";
    private SharedPreferences sharedPref;

    public Repository(Activity activity) {
        sharedPref = activity.getSharedPreferences("Sigma2", Context.MODE_PRIVATE);
    }

    public int getLastCompletedLevel() {
        TreeMap<Integer, Integer> results = getLevelsResults();
        return results.isEmpty() ? 0 : results.lastKey();
    }

    public TreeMap<Integer, Integer> getLevelsResults() {
        String scores = sharedPref.getString("scores", EMPTY_JSON);
        return convertScoresToMap(scores);
    }

    public int getLevelRank(int levelNumber){
        TreeMap<Integer, Integer> results = getLevelsResults();
        return results.containsKey(levelNumber) ? results.get(levelNumber) : 0;
    }

    public void saveLevelResult(int level, int rank) {
        SharedPreferences.Editor editor = sharedPref.edit();
        Map<Integer, Integer> results = getLevelsResults();
        results.put(level, rank);
        String scoresJson = convertScoresToJson(results);
        editor.putString("scores", scoresJson);
        editor.apply();
    }

    private String convertScoresToJson(Map<Integer, Integer> scores) {
        Gson gson = new Gson();
        return gson.toJson(scores);
    }

    private TreeMap<Integer, Integer> convertScoresToMap(String scoresStringJson) {
        Gson gson = new Gson();
        Map<String, Double> map = gson.fromJson(scoresStringJson, (Type) Map.class);

        TreeMap<Integer, Integer> result = new TreeMap<>();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            result.put(Integer.valueOf(entry.getKey()), entry.getValue().intValue());
        }

        return result;
    }

}
