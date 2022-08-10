/*
1. В одном запросе получить
- имена всех person, которые не состоят в компании с id = 5;
- название компании для каждого человека.
*/

SELECT person.name, company.name
FROM person
         JOIN company ON person.company_id = company.id
WHERE company_id != 5;

/*
2. Необходимо выбрать название компании с максимальным количеством человек + количество человек в этой компании
(нужно учесть, что таких компаний может быть несколько).
 */

SELECT company.name AS company_name, COUNT(person.id) AS amount_employee
FROM company
         JOIN person ON company.id = person.company_id
GROUP BY company_name
HAVING COUNT(person.id) = (SELECT COUNT(company_id) AS amount
                           FROM person
                           GROUP BY company_id
                           ORDER BY amount DESC
                           LIMIT 1);

