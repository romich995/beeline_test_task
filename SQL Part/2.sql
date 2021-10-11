--Вывести список сотрудников, получающих
--минимальную заработную плату в своем отделе. 
--Отразить поля: имя, должность,
--id отдела, заработная плата сотрудника 
select emp_name,
       job_name,
       dep_id,
       salary
from (
    select
        emp.emp_name,
        emp.job_name,
        emp.dep_id,
        emp.salary
        rank() over(partition by dep_id 
            order by emp.salary asc) rnk
    from employees as emp) as tmp
where rnk = 1

