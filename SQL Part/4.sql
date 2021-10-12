--Вывести список сотрудников, 
--не имеющих назначенного руководителя, 
--работающего в том же отделе. 
--Отразить поля: имя, должность, наименование отдела

select emp.emp_name,
       emp.job_name,
       dep.dep_name
from employees as emp
     left join employees as mng
        on emp.manager_id = mng.emp_id
     left join department as dep
        on emp.dep_id = dep.dep_id
where mng.dep_id <> emp.dep_id
      or mng.dep_id is null