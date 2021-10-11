--Вывести список ID отделов, 
--количество сотрудников в которых 
--превышает 3 человека 
select emp.dep_id
from emp
group by emp.dep_id
having count(*) > 3