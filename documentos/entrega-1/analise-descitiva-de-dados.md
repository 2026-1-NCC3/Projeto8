# Análise Descritiva de Dados - Entrega I
<br/>

## Nome, tipo/subtipo das variáveis necessárias

**Idade do paciente (patientAge):**  
<ins>Tipo:</ins> Quantitativa.  
<ins>Subtipo:</ins> Continua.  
<ins>Exemplos:</ins> 18, 40, 91.  

**Status do paciente (status):**  
<ins>Tipo:</ins> Qualitativa.  
<ins>Subtipo:</ins> Nominal.  
<ins>Exemplos:</ins> true ("Ativo") e false ("Inativo").  

**Categoria do exercício (tags):**  
<ins>Tipo:</ins> Qualitativa.  
<ins>Subtipo:</ins> Nominal.  
<ins>Exemplos:</ins> "Lombar", "Cervical", "Alongamento".  

**Séries de um exercício (series):**  
<ins>Tipo:</ins> Quantitativa.  
<ins>Subtipo:</ins> Discreta.  
<ins>Exemplos:</ins> 3, 4, 5.  

**Repetições por série (repetitions):**  
<ins>Tipo:</ins> Quantitativa.  
<ins>Subtipo:</ins> Discreta.  
<ins>Exemplos:</ins> 10, 15, 20.  
<br/>

## Com base nos dados simulados de cada variável, foram feitos: o cálculo da Média mais indicada para cada variável, o 95º Percentil da série de dados e sua interpretação

**Idade do paciente (patientAge):**  
<ins>Dados Simulados:</ins> 25, 30, 45, 50, 55, 60, 70, 72  
<ins>Média Mais Indicada:</ins> Média Aritmética – 50,875 anos;  
<ins>95º Percentil:</ins> 71,3 ∴ 95% dos pacientes da clínica possuem 71,3 anos ou menos, indicando que apenas 5% representam um público com idade superior.  

**Status do paciente (status):**  
<ins>Dados Simulados:</ins> Ativo, Ativo, Inativo, Ativo, Inativo, Ativo, Ativo, Inativo  
<ins>Média Mais Indicada:</ins> Moda (por se tratar de uma variável qualitativa);  
<ins>95º Percentil:</ins> não se aplica o cálculo de percentis para dados qualitativos nominais.  

**Categoria do exercício (tags):**  
<ins>Dados Simulados:</ins> "Lombar", "Cervical", "Alongamento", "Fortalecimento", etc.  
<ins>Média Mais Indicada:</ins> Moda (por se tratar de uma variável qualitativa);  
<ins>95º Percentil:</ins> não se aplica o cálculo de percentis para dados qualitativos nominais.  

**Séries de um exercício (series):**  
<ins>Dados Simulados:</ins> 1, 2, 2, 3, 3, 3, 4, 4  
<ins>Média Mais Indicada:</ins> Média Aritmética – 2,75 séries;  
<ins>95º Percentil:</ins> 4 ∴ 95% dos exercícios prescritos na clínica possuem 4 séries ou menos, indicando que apenas 5% representam um número maior de séries.  

**Repetições por série (repetitions):**  
<ins>Dados Simulados:</ins> 10, 10, 15, 15, 20, 20, 25, 25  
<ins>Média Mais Indicada:</ins> Média Aritmética – 17,5 repetições;  
<ins>95º Percentil:</ins>  25 ∴ 95% das séries prescritas na clínica possuem 25 repetições ou menos, indicando que apenas 5% representam um número maior de repetição.  

★ Na prática, optou-se por calcular a Média Ponderada das repetições utilizando o número de 'Séries' como peso. Em função de que, na RPG, o esforço de um exercício não é medido pelas repetições isoladas, mas pelo Volume Total (Séries x Repetições), revelando a carga média real de repetições por bloco de exercício prescrito pela clínica.
<br/>

```
Fórmulas utilizadas no Excel
Média: =MED(INICIO:FIM)
Percentil: =PERCENTIL.INC(INICIO:FIM; PERCENTIL(º))
```
<br/>

## Gráficos
![Graficos_2](https://github.com/user-attachments/assets/a3f2cd80-1765-44a3-9d8b-1d01535a385b)
![Graficos_1](https://github.com/user-attachments/assets/f0219474-f1ed-42fc-b9ca-5975b0433e71)
![Graficos_1](https://github.com/user-attachments/assets/2cf08d64-a876-4c85-ad0e-4a17cd4d75b7)

