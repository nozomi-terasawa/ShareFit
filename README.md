# ブランチルール
・デフォルトブランチはdevelop-main、各develop/**ブランチからdevelop-mainに向けてPRを立てること

・ブランチを新規作成するときはdevelop-mainからdevelop/**ブランチを切ること。（develop-main ---> develop/android ）

・develop/**から画面単位でfeature/**ブランチを切ること。（develop/android --> feature/todo_screen）

・feature/**から先のブランチは機能単位で同様にfeature/**で自由にブランチを切ってOK

## イメージ

<img width="386" alt="スクリーンショット 2024-10-06 18 57 03" src="https://github.com/user-attachments/assets/6bee673d-06ec-461f-ab35-a5b185770e88">

# push前にすること
## kotlin 

ターミナルで `ktlint --format` を実行（Kotlinのフォーマットに沿ってコードを綺麗にしてくれる）
インストールは下記参照
https://github.com/pinterest/ktlint?tab=readme-ov-file

# Github Actionsが動くケース
・feature/**に向かってpushしたとき

・develop-mainに向かってPRを立てたとき

基本的にはブランチルールを守っていれば動くはず
