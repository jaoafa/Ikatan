package com.jaoafa.Ikatan;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dv8tion.jda.api.entities.Activity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Task_GetIka extends TimerTask {
	@Override
	public void run() {
		try {
			String url = "https://spla2.yuu26.com/schedule";
			OkHttpClient okclient = new OkHttpClient();
			Request request = new Request.Builder().url(url).build();
			Response response = okclient.newCall(request).execute();
			String res = response.body().string();
			JSONObject json = new JSONObject(res);
			JSONObject result = json.optJSONObject("result");
			if (result == null) {
				System.out.println("[Task_GetIka] result not found: " + res);
				return;
			}
			JSONArray regularArr = result.optJSONArray("regular");
			JSONObject now = regularArr.getJSONObject(0);
			String now_rule = now.getString("rule");
			JSONArray now_maps = now.getJSONArray("maps");
			List<String> now_maplist = new ArrayList<>();
			for (int i = 0; i < now_maps.length(); i++) {
				now_maplist.add(now_maps.getString(i));
			}
			Long now_start = now.optLong("start_t");
			Long now_end = now.optLong("end_t");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			Main.getJDA().getPresence().setActivity(
					Activity.playing(
							String.join(", ", now_maplist) + " [" + now_rule + "] ("
									+ sdf.format(new Date(now_start * 1000)) + " ～ "
									+ sdf.format(new Date(now_end * 1000)) + ")"));

		} catch (IOException e) {
			System.out.println("[Task_GetIka] IOException: " + e.getMessage());
			return;
		}
	}
}
