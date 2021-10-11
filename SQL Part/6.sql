--6.  Определить количество сотрудников, 
--относящихся к каждому уровню заработной платы, 
--отсортировать по убыванию

select grd.grade,
       count(emp.emp_id) count_employees
from grade_salary as grd
     left join employees as emp
        on emp.salary between grd.min_salary
             and grd.max_salary
group by grd.grade
order by 2 desc

