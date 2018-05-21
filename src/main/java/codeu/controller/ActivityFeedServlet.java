// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import java.util.List;
import java.io.IOException;
import java.time.Instant;//unsused for now
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet class responsible for Activity feed page */
public class ActivityFeedServlet extends HttpServlet{
	/** variable used to keep track of activity, I'm thinking to create more variables as we go to register different types of activities */
	private List<String> activity;
	/** used to keep track of users friends*/
	private List<String> friends_usernames;
	@Override
	public void init() throws ServletException{
		super.init(); 

		//hardocded activity for now
		activity = Arrays.asList('Ricardo Joined', 'Elona says hi', 'Drew just left the conversation');
		friends_usernames = Arrays.asList('Ricardo', 'Elona', 'Drew');
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException){
		String username = (String) request.getSession().getAtrribute('user');

			request.getRequestDispatcher("/WEB-INF/view/activity.jsp").forward(request, response);
	}
}