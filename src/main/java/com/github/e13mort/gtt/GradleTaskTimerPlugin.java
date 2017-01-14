package com.github.e13mort.gtt;

import org.gradle.BuildAdapter;
import org.gradle.BuildResult;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.tasks.TaskState;
import org.gradle.util.Clock;

import java.util.ArrayList;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class GradleTaskTimerPlugin implements Plugin<Project> {

    static final String OUT_INTRO = "Task timings (took longer than %s ms):\n";
    static final String OUT_TIMING_TEMPLATE = "%7sms  %s\n";
    private static final String SETTINGS = "taskTimingSettings";

    @Override
    public void apply(Project project) {
        project.getExtensions().create(SETTINGS, TaskPluginExtension.class);
        project.getGradle().addBuildListener(new TimingListener(project));
    }

    private static class TimingListener extends BuildAdapter implements TaskExecutionListener {

        private final Project project;
        private Clock clock;
        private ArrayList<TaskTimingInfo> timings = new ArrayList<>();

        TimingListener(Project project) {
            this.project = project;
        }

        @Override
        public void beforeExecute(Task task) {
            clock = new Clock();
        }

        @Override
        public void afterExecute(Task task, TaskState taskState) {
            timings.add(new TaskTimingInfo(task.getName(), clock.getTimeInMs()));
        }

        @Override
        public void buildFinished(BuildResult result) {
            long threshold = getThreshold();
            log(OUT_INTRO, threshold);
            for (TaskTimingInfo timing : timings) {
                if (timing.timeInMs >= threshold) {
                    log(OUT_TIMING_TEMPLATE, timing.timeInMs, timing.name);
                }
            }
        }

        private long getThreshold() {
            TaskPluginExtension extension = project.getExtensions().findByType(TaskPluginExtension.class);
            //noinspection ConstantConditions
            return extension != null ? extension.getThreshold() : TaskPluginExtension.DEFAULT_THRESHOLD;
        }

        private static class TaskTimingInfo {

            private final String name;
            private final long timeInMs;

            TaskTimingInfo(String name, long timeInMs) {
                this.name = name;
                this.timeInMs = timeInMs;
            }
        }
    }

    private static void log(String format, Object... params) {
        System.out.printf(format, params);
    }
}
