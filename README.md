# 1-2-3 Diagnoz app

Определение совместимости партнеров с помощью быстрого глубинного анализа диалога. На вход можно подать естественную речь или переписку в социальной сети. На выходе определяется процент совместимости и сферы проблем в отношениях. Алгоритм частотного анализа речи авторский, создан на основе теории Л.С. Выготского.

# API
## Request 
{
  "speech1": {
    "length": 264,
    "text": "Hello! How are you! Great thanks, meeting today?"
  },
  "speech2": {
    "length": 3654,
    "text": "Hi! Nice, and you? Yeah, See you"
  }
}
## Response
{
  "contexts":[
      {"relationship_type":"development","percent":55.55},
      {"relationship_type":"emotions","percent":60.0},
      {"relationship_type":"leadership","percent":55.55},
      {"relationship_type":"trusty","percent":20.0},
      {"relationship_type":"base","percent":50.0}
    ]
}
