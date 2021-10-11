--Вывести список сотрудников, получающих 
--заработную плату большую чем у непосредственного руководителя.
--Отразить поля: имя, должность, id отдела, 
--заработная плата сотрудника, зарплата руководителя select emp.emp_name,
select mp.emp_name,
       emp.job_name,
       emp.dep_id,
       emp.salary,
       mng.salary
from employees as emp
     join employee as mng
         on emp.manager_id = mng.emp_id
where emp.salary > mng.salary 