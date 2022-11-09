# 포인트 적립 시스템 API 개발 내용입니다.

## 사용 기술 스택
- java11, QueryDsl, Junit5, SpringBoot, Spring RestDoc, h2

## 설계내용
- [링크]()

<br/>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="generator" content="Asciidoctor 2.0.10">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open+Sans:300,300italic,400,400italic,600,600italic%7CNoto+Serif:400,400italic,700,700italic%7CDroid+Sans+Mono:400,700">

</head>
<body class="article">
<div id="header">
<h1>포인트 API</h1>
<div class="details">
<span id="revnumber">version 0.0.1-SNAPSHOT</span>
</div>
<div id="toc" class="toc">
<div id="toctitle">Table of Contents</div>
<ul class="sectlevel1">
<li><a href="#_포인트_계좌_관련">포인트 계좌 관련</a>
<ul class="sectlevel2">
<li><a href="#_포인트_계좌_생성">포인트 계좌 생성</a></li>
<li><a href="#_포인트_계좌_삭제">포인트 계좌 삭제</a></li>
<li><a href="#_포인트_계좌_조회_포인트_총액_조회">포인트 계좌 조회( 포인트 총액 조회 )</a></li>
</ul>
</li>
<li><a href="#_포인트_적립사용">포인트 적립/사용</a>
<ul class="sectlevel2">
<li><a href="#_포인트_적립사용_2">포인트 적립/사용</a></li>
</ul>
</li>
<li><a href="#_포인트_사용_취소">포인트 사용 취소</a>
<ul class="sectlevel2">
<li><a href="#_포인트_사용_취소_2">포인트 사용 취소</a></li>
</ul>
</li>
<li><a href="#_포인트_이벤트_조회_단건">포인트 이벤트 조회 ( 단건 )</a>
<ul class="sectlevel2">
<li><a href="#_포인트_이벤트_조회">포인트 이벤트 조회</a></li>
</ul>
</li>
<li><a href="#_포인트_이벤트이벤트_상세_조회_다수건_페이징_처리">포인트 이벤트,이벤트 상세 조회 ( 다수건 페이징 처리 )</a>
<ul class="sectlevel2">
<li><a href="#_포인트_이벤트_조회_2">포인트 이벤트 조회</a></li>
<li><a href="#_포인트_이벤트_상세_조회">포인트 이벤트 상세 조회</a></li>
</ul>
</li>
</ul>
</div>
</div>
<div id="content">
<div class="sect1">
<h2 id="_포인트_계좌_관련">포인트 계좌 관련</h2>
<div class="sectionbody">
<div class="sect2">
<h3 id="_포인트_계좌_생성">포인트 계좌 생성</h3>
<div class="sect3">
<h4 id="_요청">요청</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight"><code class="language-bash" data-lang="bash">$ http POST 'https://api.PointReserve.com/reserves/create/1' \
    'Content-Type:application/json;charset=UTF-8'</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<caption class="title">Table 1. /reserves/create/{memberId}</caption>
<colgroup>
<col style="width: 50%;">
<col style="width: 50%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Parameter</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_응답">응답</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>{"id":3,"memberId":1,"totalAmount":0}</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>id</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 계좌(Account) ID</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>totalAmount</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 총계(최초 가입시 0원)</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_예외">예외</h4>
<div class="ulist">
<ul>
<li>
<p>동일한 회원번호로 이미 포인트 계좌가 존재하는 경우 예외가 발생합니다.</p>
</li>
</ul>
</div>
<div class="listingblock">
<div class="content">
<pre>{"code":"409","message":"이미 존재하는 계정입니다. (향후 하나의 계정이 여러 포인트를 사용하도록 개선 예정입니다.)","validation":{"errorResponse":"이미 존재하는 계정입니다. (향후 하나의 계정이 여러 포인트를 사용하도록 개선 예정입니다.)"}}</pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>code</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">409 에러코드를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>message</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'이미 존재하는 계정입니다. (향후 하나의 계정이 여러 포인트를 사용하도록 개선 예정입니다.)' 메시지를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>validation.errorResponse</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.</p></td>
</tr>
</tbody>
</table>
</div>
</div>
<div class="sect2">
<h3 id="_포인트_계좌_삭제">포인트 계좌 삭제</h3>
<div class="sect3">
<h4 id="_요청_2">요청</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight"><code class="language-bash" data-lang="bash">$ http DELETE 'https://api.PointReserve.com/reserves/delete/1' \
    'Content-Type:application/json;charset=UTF-8'</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<caption class="title">Table 2. /reserves/delete/{memberId}</caption>
<colgroup>
<col style="width: 50%;">
<col style="width: 50%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Parameter</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_응답_2">응답</h4>
<div class="ulist">
<ul>
<li>
<p>응답 없음</p>
</li>
</ul>
</div>
</div>
<div class="sect3">
<h4 id="_예외_2">예외</h4>
<div class="ulist">
<ul>
<li>
<p>존재하지 않는 계좌의 삭제를 요청하는 경우 아래 예외가 발생합니다.</p>
</li>
</ul>
</div>
<div class="listingblock">
<div class="content">
<pre>{"code":"404","message":"존재하지 않는 계좌입니다.","validation":{"errorMessage":"존재하지 않는 계좌입니다."}}</pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>code</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">404 error를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>message</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'존재하지 않는 계좌입니다.' 문구를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>validation.errorMessage</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.</p></td>
</tr>
</tbody>
</table>
</div>
</div>
<div class="sect2">
<h3 id="_포인트_계좌_조회_포인트_총액_조회">포인트 계좌 조회( 포인트 총액 조회 )</h3>
<div class="sect3">
<h4 id="_요청_3">요청</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight"><code class="language-bash" data-lang="bash">$ http GET 'https://api.PointReserve.com/reserves/get/1' \
    'Content-Type:application/json;charset=UTF-8'</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<caption class="title">Table 3. /reserves/get/{memberId}</caption>
<colgroup>
<col style="width: 50%;">
<col style="width: 50%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Parameter</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_응답_3">응답</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>{"id":2,"memberId":1,"totalAmount":100}</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>id</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 계좌(Account) ID</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>totalAmount</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 총계(현재 가지고 있는 포인트의 합을 반환합니다.)</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_예외_3">예외</h4>
<div class="ulist">
<ul>
<li>
<p>존재하지 않는 계좌의 정보를 요청하는 경우 아래 예외가 발생합니다.</p>
</li>
</ul>
</div>
<div class="listingblock">
<div class="content">
<pre>{"code":"404","message":"존재하지 않는 계좌입니다.","validation":{"errorMessage":"존재하지 않는 계좌입니다."}}</pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>code</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">404를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>message</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'존재하지 않는 계좌입니다.' 문구를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>validation.errorMessage</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.</p></td>
</tr>
</tbody>
</table>
</div>
</div>
</div>
</div>
<div class="sect1">
<h2 id="_포인트_적립사용">포인트 적립/사용</h2>
<div class="sectionbody">
<div class="sect2">
<h3 id="_포인트_적립사용_2">포인트 적립/사용</h3>
<div class="ulist">
<ul>
<li>
<p>포인트 적립과 사용 관련한 상태정보는 총 2가지 입니다.</p>
</li>
<li>
<p>SAVE_UP : 적립</p>
</li>
<li>
<p>REDEEM : 사용</p>
</li>
<li>
<p>적립은 전체금액에 해당 금액만큼을 더하고, 사용은 해당 금액만큼 차감합니다.</p>
</li>
<li>
<p>적립금액의 최대 한도는 1,000,000임을 가정하였습니다.</p>
</li>
</ul>
</div>
<div class="sect3">
<h4 id="_요청_4">요청</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight"><code class="language-bash" data-lang="bash">$ echo '{"memberId":1,"amount":10,"reservesStatus":"REDEEM"}' | http POST 'https://api.PointReserve.com/reserves/event/create' \
    'Content-Type:application/json;charset=UTF-8'</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>amount</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립 금액 혹은 사용 금액입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>reservesStatus</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립, 사용 상태를 나타냅니다.</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_응답_4">응답</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>{"id":"e-1","memberId":1,"amount":10,"status":"REDEEM","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"}</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>id</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 이벤트 ID</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>amount</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립 금액 혹은 사용 금액입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>status</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립, 사용 상태를 나타냅니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>effectiveData</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 이벤트가 발생한 시점으로 적립금의 효력이 시작되는 날짜입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>expiryDate</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트의 효력이 상실되는 만료일입니다. 시작일 +1년</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_예외_4">예외</h4>
<div class="ulist">
<ul>
<li>
<p>포인트 총합이 0원 미만이 되도록 하는 요청에 대해 예외를 발생합니다.</p>
</li>
<li>
<p>포인트는 1,000,000원이 최대 적립 가능 금액입니다. 이를 초과 시 예외를 발생합니다.</p>
</li>
</ul>
</div>
<div class="listingblock">
<div class="content">
<pre>{"code":"400","message":"잘못된 요청입니다.","validation":{"amount":"금액은 0원 이하가 될 수 없습니다."}}</pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>code</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">400을 리턴합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>message</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'잘못된 요청입니다.' 문구를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>validation.amount</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'금액은 0원 이하가 될 수 없습니다.' 문구를 반환합니다.</p></td>
</tr>
</tbody>
</table>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>{"code":"400","message":"잘못된 요청입니다.","validation":{"amount":"적립금액은 1,000,000원을 초과 할 수 없습니다."}}</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>code</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">400을 리턴합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>message</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'잘못된 요청입니다.' 문구를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>validation.amount</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'적립금액은 1,000,000원을 초과 할 수 없습니다.' 문구를 반환합니다.</p></td>
</tr>
</tbody>
</table>
</div>
</div>
</div>
</div>
<div class="sect1">
<h2 id="_포인트_사용_취소">포인트 사용 취소</h2>
<div class="sectionbody">
<div class="sect2">
<h3 id="_포인트_사용_취소_2">포인트 사용 취소</h3>
<div class="sect3">
<h4 id="_요청_5">요청</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight"><code class="language-bash" data-lang="bash">$ echo '{"memberId":1,"eventId":"e-2","reservesStatus":"CANCLE_REDEEM"}' | http POST 'https://api.PointReserve.com/reserves/event/cancel' \
    'Content-Type:application/json;charset=UTF-8'</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>eventId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립 사용을 취소할 ID입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>reservesStatus</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">사용 취소 상태를 나타냅니다.</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_응답_5">응답</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>{"id":"e-3","memberId":1,"amount":0,"status":"CANCLE_REDEEM","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"}</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>id</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 이벤트 ID</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>amount</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">사용 취소의 경우 0원을 표시합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>status</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">사용 취소 상태를 나타냅니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>effectiveData</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">취소된 포인트 이벤트가 발생한 시점으로 포인트의 효력이 시작되는 날짜입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>expiryDate</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">취소된 포인트의 효력이 상실되는 만료일입니다. 시작일 +1년</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_예외_5">예외</h4>
<div class="ulist">
<ul>
<li>
<p>취소하고자 하는 대상 이벤트가 없으면 아래 에러를 반환합니다.</p>
</li>
</ul>
</div>
<div class="listingblock">
<div class="content">
<pre>{"code":"404","message":"존재하지 않는 Event입니다.","validation":{"errorResponse":"존재하지 않는 Event입니다."}}</pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>code</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">404를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>message</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'존재하지 않는 Event입니다.' 문구를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>validation.errorResponse</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.</p></td>
</tr>
</tbody>
</table>
</div>
</div>
</div>
</div>
<div class="sect1">
<h2 id="_포인트_이벤트_조회_단건">포인트 이벤트 조회 ( 단건 )</h2>
<div class="sectionbody">
<div class="sect2">
<h3 id="_포인트_이벤트_조회">포인트 이벤트 조회</h3>
<div class="sect3">
<h4 id="_요청_6">요청</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight"><code class="language-bash" data-lang="bash">$ http GET 'https://api.PointReserve.com/reserves/events/get/e-1' \
    'Content-Type:application/json;charset=UTF-8'</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<caption class="title">Table 4. /reserves/events/get/{eventId}</caption>
<colgroup>
<col style="width: 50%;">
<col style="width: 50%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Parameter</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>eventId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립금 이벤트 ID</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_응답_6">응답</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>{"id":"e-1","memberId":1,"amount":10,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"}</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>id</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 이벤트 ID</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>amount</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립 금액 혹은 사용 금액입니다. 취소금액은 0입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>status</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립, 사용, 적립 취소 상태를 나타냅니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>effectiveData</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 이벤트가 발생한 시점으로 포인트의 효력이 시작되는 날짜입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>expiryDate</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트의 효력이 상실되는 만료일입니다. 시작일 +1년</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_예외_6">예외</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>{"code":"404","message":"존재하지 않는 Event입니다.","validation":{"errorResponse":"존재하지 않는 Event입니다."}}</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>code</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">404를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>message</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'존재하지 않는 Event입니다.' 문구를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>validation.errorResponse</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.</p></td>
</tr>
</tbody>
</table>
</div>
</div>
</div>
</div>
<div class="sect1">
<h2 id="_포인트_이벤트이벤트_상세_조회_다수건_페이징_처리">포인트 이벤트,이벤트 상세 조회 ( 다수건 페이징 처리 )</h2>
<div class="sectionbody">
<div class="sect2">
<h3 id="_포인트_이벤트_조회_2">포인트 이벤트 조회</h3>
<div class="sect3">
<h4 id="_요청_7">요청</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight"><code class="language-bash" data-lang="bash">$ http GET 'https://api.PointReserve.com/reserves/events/getList?page=2&amp;size=10&amp;memberId=1' \
    'Content-Type:application/json;charset=UTF-8'</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 50%;">
<col style="width: 50%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Parameter</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>page</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">조회하고자 하는 페이지 번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>size</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">한 페이지에 조회할 개수</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_응답_7">응답</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>[{"id":"e-11","memberId":1,"amount":11,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"},{"id":"e-12","memberId":1,"amount":12,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"},{"id":"e-13","memberId":1,"amount":13,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"},{"id":"e-14","memberId":1,"amount":14,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"},{"id":"e-15","memberId":1,"amount":15,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"},{"id":"e-16","memberId":1,"amount":16,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"},{"id":"e-17","memberId":1,"amount":17,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"},{"id":"e-18","memberId":1,"amount":18,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"},{"id":"e-19","memberId":1,"amount":19,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"},{"id":"e-20","memberId":1,"amount":20,"status":"SAVEUP","effectiveData":"2022-11-08 08:04:17","expiryDate":"2023-11-08 08:04:17"}]</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].id</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 이벤트 상세 ID</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].status</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립, 사용, 적립 취소 상태를 나타냅니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].amount</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립 금액 혹은 사용 금액입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].effectiveData</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 이벤트가 발생한 시점으로 포인트의 효력이 시작되는 날짜입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].expiryDate</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트의 효력이 상실되는 만료일입니다. 시작일 +1년</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_예외_7">예외</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>{"code":"404","message":"존재하지 않는 Event입니다.","validation":{"errorResponse":"존재하지 않는 Event입니다."}}</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>code</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">404를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>message</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'존재하지 않는 Event입니다.' 문구를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>validation.errorResponse</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.</p></td>
</tr>
</tbody>
</table>
</div>
</div>
<div class="sect2">
<h3 id="_포인트_이벤트_상세_조회">포인트 이벤트 상세 조회</h3>
<div class="sect3">
<h4 id="_요청_8">요청</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight"><code class="language-bash" data-lang="bash">$ http GET 'https://api.PointReserve.com/reserves/eventsDetail/getList?page=1&amp;size=10&amp;memberId=1' \
    'Content-Type:application/json;charset=UTF-8'</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 50%;">
<col style="width: 50%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Parameter</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>page</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">조회하고자 하는 페이지 번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>size</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">한 페이지에 조회할 개수</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>memberId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_응답_8">응답</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>[{"id":"d-1","membershipId":1,"status":"SAVEUP","amount":1,"eventId":"e-1","signUpId":null,"cancelId":null,"effectiveData":"2022-11-08T08:04:14.190983","expiryDate":"2023-11-08T08:04:14.190983"},{"id":"d-2","membershipId":1,"status":"SAVEUP","amount":2,"eventId":"e-2","signUpId":null,"cancelId":null,"effectiveData":"2022-11-08T08:04:14.200661","expiryDate":"2023-11-08T08:04:14.200661"},{"id":"d-3","membershipId":1,"status":"SAVEUP","amount":3,"eventId":"e-3","signUpId":null,"cancelId":null,"effectiveData":"2022-11-08T08:04:14.203741","expiryDate":"2023-11-08T08:04:14.203741"},{"id":"d-4","membershipId":1,"status":"SAVEUP","amount":4,"eventId":"e-4","signUpId":null,"cancelId":null,"effectiveData":"2022-11-08T08:04:14.206996","expiryDate":"2023-11-08T08:04:14.206996"},{"id":"d-5","membershipId":1,"status":"SAVEUP","amount":5,"eventId":"e-5","signUpId":null,"cancelId":null,"effectiveData":"2022-11-08T08:04:14.212078","expiryDate":"2023-11-08T08:04:14.212078"},{"id":"d-6","membershipId":1,"status":"SAVEUP","amount":6,"eventId":"e-6","signUpId":null,"cancelId":null,"effectiveData":"2022-11-08T08:04:14.216909","expiryDate":"2023-11-08T08:04:14.216909"},{"id":"d-7","membershipId":1,"status":"SAVEUP","amount":7,"eventId":"e-7","signUpId":null,"cancelId":null,"effectiveData":"2022-11-08T08:04:14.220459","expiryDate":"2023-11-08T08:04:14.220459"},{"id":"d-8","membershipId":1,"status":"SAVEUP","amount":8,"eventId":"e-8","signUpId":null,"cancelId":null,"effectiveData":"2022-11-08T08:04:14.224042","expiryDate":"2023-11-08T08:04:14.224042"},{"id":"d-9","membershipId":1,"status":"SAVEUP","amount":9,"eventId":"e-9","signUpId":null,"cancelId":null,"effectiveData":"2022-11-08T08:04:14.228803","expiryDate":"2023-11-08T08:04:14.228803"},{"id":"d-10","membershipId":1,"status":"SAVEUP","amount":10,"eventId":"e-10","signUpId":null,"cancelId":null,"effectiveData":"2022-11-08T08:04:14.234471","expiryDate":"2023-11-08T08:04:14.234471"}]</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].id</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 이벤트 상세 ID</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].membershipId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">회원번호</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].status</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립, 사용, 적립 취소 상태를 나타냅니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].amount</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Number</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">적립 금액 혹은 사용 금액입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].eventId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">해당 이벤트 상세를 포함하는 이벤트의 id입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].signUpId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Null</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 사용 시, 적립 이벤트 상세의 id입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].cancelId</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Null</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 사용 취소시, 사용 이벤트 상세의 id입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].effectiveData</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트 이벤트가 발생한 시점으로 포인트의 효력이 시작되는 날짜입니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>[].expiryDate</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">포인트의 효력이 상실되는 만료일입니다. 시작일 +1년</p></td>
</tr>
</tbody>
</table>
</div>
<div class="sect3">
<h4 id="_예외_8">예외</h4>
<div class="listingblock">
<div class="content">
<pre class="highlight nowrap"><code>{"code":"404","message":"존재하지 않는 상세정보입니다.","validation":{"errorResponse":"존재하지 않는 상세정보입니다."}}</code></pre>
</div>
</div>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33.3333%;">
<col style="width: 33.3333%;">
<col style="width: 33.3334%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Path</th>
<th class="tableblock halign-left valign-top">Type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>code</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">404를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>message</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">'존재하지 않는 상세정보입니다.' 문구를 반환합니다.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>validation.errorResponse</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>String</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.</p></td>
</tr>
</tbody>
</table>
</div>
</div>
</div>
</div>
</div>
<div id="footer">
<div id="footer-text">
Version 0.0.1-SNAPSHOT<br>
Last updated 2022-11-08 07:57:34 +0900
</div>
</div>
</body>
</html>
