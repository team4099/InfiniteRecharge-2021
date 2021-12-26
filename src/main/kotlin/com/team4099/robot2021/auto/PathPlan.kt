package com.team4099.robot2021.auto

import com.pathplanner.lib.PathPlanner
import com.pathplanner.lib.PathPlannerTrajectory
import com.team4099.robot2021.config.Constants

object PathPlan {
  var examplePath: PathPlannerTrajectory = PathPlanner.loadPath("Example Path", Constants.Drivetrain.MAX_AUTO_VEL.value, Constants.Drivetrain.MAX_AUTO_ACCEL.value)
}
