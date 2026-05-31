package com.taeyoonkim.model;

import java.util.List;

public interface ILessonDAO {
    boolean insertLesson(LessonDTO lesson);
    List<LessonDTO> getAllLessons();
    boolean updateLesson(LessonDTO lesson);
    boolean deleteLesson(int id);
}
