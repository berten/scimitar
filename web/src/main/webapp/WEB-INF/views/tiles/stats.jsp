<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="panel panel-default">
    <div class="panel-heading">
        <i class="fa fa-bar-chart-o fa-fw"></i> Stats
        Example
        <div class="pull-right">
            <div class="btn-group">
                <button type="button"
                    class="btn btn-default btn-xs dropdown-toggle"
                    data-toggle="dropdown">
                    Actions
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu pull-right"
                    role="menu">
                    <li><a href="#">Analyse</a>
                    </li>
                    <li><a href="#">Print</a>
                    </li>
                    <li class="divider"></li>
                    <li><a href="#">Edit</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!-- /.panel-heading -->
    <div class="panel-body">
        <div class="row">
            <div class="col-lg-12">

            </div>
        </div>
        <c:forEach items="${stats}" var="race">
        <div class="row">
            <div class="col-lg-12">
                <h1> ${race.race}</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">

                <div class="table-responsive">
                    <table
                        class="table table-bordered table-hover table-striped">
                        <thead>
                        <tr>

                        <tr>
                            <th>Name</th>
                            <th>Class</th>
                            <th> T1</th>
                            <th> T2</th>
                            <th> T3</th>
                            <th>Type</th>
                            <th>Init</th>
                            <th>Gun</th>
                            <th>Arm</th>
                            <th>Dmg</th>
                            <th>E/R</th>
                            <th>M</th>
                            <th>C</th>
                            <th>E</th>
                            <th>A/C</th>
                            <th>D/C</th>

                        </thead>
                        <tbody>
                        <c:forEach items="${race.ships}" var="ship">
                            <tr>
                                <td>${ship.name}</td>
                                <td>${ship.klaske}</td>
                                <td>${ship.target1}</td>
                                <td>${ship.target2}</td>
                                <td>${ship.target3}</td>
                                <td>${ship.type}</td>
                                <td>${ship.initiative}</td>
                                <td>${ship.guns}</td>
                                <td>${ship.armor}</td>
                                <td>${ship.damage}</td>
                                <td>${ship.empres}</td>
                                <td>${ship.metal}</td>
                                <td>${ship.crystal}</td>
                                <td>${ship.eonium}</td>
                                <td>${ship.armorcost}</td>
                                <td>${ship.damagecost}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                </c:forEach>
                <!-- /.table-responsive -->
            </div>

            <!-- /.col-lg-8 (nested) -->
        </div>
        <!-- /.row -->
    </div>
    <!-- /.panel-body -->
</div>