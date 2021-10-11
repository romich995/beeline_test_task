--Ранжировать сотрудников в каждом отделе 
--по стажу работы в днях до текущей даты 
--(current_date) по убывающей. 
--Отразить поля: имя, должность, id отдела, 
--стаж в днях, ранг

 select emp.emp_name,
        emp.job_name,
        emp.dep_id,
        DATEDIFF(DAY, current_date, emp.hire_date) as total_days,
        rank() over(partition by emp.dep_id
         order by emp.hire_date) as rnk
 from employees as emp
 order by emp.dep_id, emp.hire_date