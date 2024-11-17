# Mobile Programming with Java
This is the result of maze game app with DFS and BFS algorithm.


## Motivation
We developed this maze game app to apply graph and backtracking theory concepts from our algorithm class, specifically Depth-First Search (DFS) and Breadth-First Search (BFS). 
These algorithms are fundamental components of graph theory, which is directly utilized in the maze-solving process as it involves exploring and traversing a graph structure. 
Furthermore, the process of retracing steps to find a path within the maze demonstrates the practical application of backtracking, an essential concept in algorithm design. </br></br>
And this project allows us to deepen our understanding of these theories through hands-on implementation and real-world problem solving. </br>

## Project Structure

For simplicity, We have implemented class diagrams with class names only. </br>
![image](https://github.com/user-attachments/assets/f156158c-de9a-4f84-9108-a6d63d842661) </br>

1. MainActivity.java: </br>
-  MainActivity serves as the start screen, providing a button that navigates to UserInputActivity.</br>

2. UserInputActivity.java: </br>
-  UserInputActivity allows the user to input the number of rows and columns for the maze, then starts MazeActivity with the specified dimensions.</br>

3. MazeActivity.java: </br>
-  MazeActivity generates a maze with the specified size, provides functionality for character movement, and allows pathfinding to the exit.</br>

4. MazeView.java: </br>
-  MazeView is a custom view used to visually display the maze generated in MazeActivity.</br>

5. SolutionActivity.java: </br>
-  SolutionActivity displays the shortest path found in MazeActivity on the screen.</br>


## Output
* **Demo Video** (**A little loading!**) </br>

https://github.com/user-attachments/assets/d75e8bc5-450a-4441-87d1-1c58862c4d72 

</br>

## 👥 Team Member
201934219 Kim Joonhee </br>
202135527 Kim Hyoshin </br>
202132289 Chong Jaewon </br>
202135596 Hyeon Gwan</br>

 
## ✔️ Source
* ChatGPT </br>
* [BFS / DFS](https://velog.io/@suk13574/%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98Java-BFS-DFS) </br>
* [Maze Stack Implementation](https://m.blog.naver.com/luckyvicky-v/221346988717) </br>
* [Android Studio Button Count](https://deumdroid.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%8A%A4%ED%8A%9C%EB%94%94%EC%98%A4-%EB%B2%84%ED%8A%BC-%ED%81%B4%EB%A6%AD-%ED%9A%9F%EC%88%98%EC%97%90-%EB%94%B0%EB%9D%BC-%EC%B9%B4%EC%9A%B4%ED%8A%B8-%EC%A6%9D%EA%B0%80) </br>
* [Android Studio GridLayout](https://lktprogrammer.tistory.com/136) </br>
* [Android Studio OnClickListener](https://onedaycodeing.tistory.com/61#google_vignette) </br>
