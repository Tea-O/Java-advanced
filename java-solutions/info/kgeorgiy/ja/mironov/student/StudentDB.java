package info.kgeorgiy.ja.mironov.student;

import info.kgeorgiy.java.advanced.student.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StudentDB implements StudentQuery {

    private final static Comparator<Student> STUDENT_COMPARATOR = Comparator
            .comparing(Student::getLastName, Comparator.reverseOrder())
            .thenComparing(Student::getFirstName, Comparator.reverseOrder())
            .thenComparing(Student::compareTo);

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getMappedList(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getMappedList(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return getMappedList(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getMappedList(students, student -> student.getFirstName() + " " + student.getLastName());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream()
                .map(Student::getFirstName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream()
                .max(Student::compareTo)
                .map(Student::getFirstName)
                .orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return getSortedStudents(students, Student::compareTo);
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return getSortedStudents(students, STUDENT_COMPARATOR);
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, final String name) {
        return getFilteredStudents(students, student -> Objects.equals(name, student.getFirstName()));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, final String name) {
        return getFilteredStudents(students, student -> Objects.equals(name, student.getLastName()));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, final GroupName group) {
        return getFilteredStudents(students, student -> Objects.equals(group, student.getGroup()));
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, final GroupName group) {
        return students.stream()
                .filter(student -> Objects.equals(student.getGroup(), group))
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)));
    }

    private <T> List<T> getMappedList(List<Student> students, Function<Student, T> mapper) {
        return students.stream()
                .map(mapper)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Student> getSortedStudents(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream()
                .sorted(comparator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Student> getFilteredStudents(Collection<Student> students, Predicate<Student> predicate) {
        return students.stream()
                .filter(predicate)
                .sorted(STUDENT_COMPARATOR)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}