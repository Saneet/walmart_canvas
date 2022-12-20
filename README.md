# Overview
Reimplemented the project with more features.  
[Video Demo on Youtube](https://www.youtube.com/shorts/7ev4QuS4hrc)

# Requirements
We are going to build a simple location-based game.   
You task is to create a geo-fence boundary and display a blue dot on a canvas, when the blue dot enters the geo-fence region, color of dot should change from blue to green and when it exits the region the color should change back to blue.   
Please code your solution in Kotlin.

# Implementation
## Calculation
1. Each Animator's Update event is sent to a SharedFlow. Each TouchEvent is sent to another SharedFlow.
2. Both these SharedFlows are combined and then sampled by 5ms to get a series of calc events.
3. On these resultant events we calculate the color of the pointer using it's latest location from TouchEvents flow.
4. The result is sent to a StateFlow that holds its latest value to be used in draw().

NOTE: The 5ms sampling would seem to cause a lag at first. But as more shapes are added it helps streamline the processing load. Even with dozens of polygons moving around the pointer's performance stays consistent.

## Animations
1. Used 2 ObjectAnimators/shape for left and top coordinates. This way the Shapes have independent trajectories.
2. Randomized the size and animation speed for each axis.
3. Bouncing between walls is done by setting each ObjectAnimator values as 0..end and using REVERSE repeat mode to oscillate.

